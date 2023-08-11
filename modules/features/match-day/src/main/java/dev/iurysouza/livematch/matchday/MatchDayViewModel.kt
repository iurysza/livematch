package dev.iurysouza.livematch.matchday

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MVIViewModel
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.footballinfo.domain.FetchMatchesInfoUseCase
import dev.iurysouza.livematch.matchday.models.MatchDayState
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent
import dev.iurysouza.livematch.matchday.models.MatchDayViewState
import dev.iurysouza.livematch.matchday.models.createMatchThreadFrom
import dev.iurysouza.livematch.matchday.models.getValidMatchList
import dev.iurysouza.livematch.matchday.models.toDestination
import dev.iurysouza.livematch.matchday.models.toMatchEntity
import dev.iurysouza.livematch.reddit.domain.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.reddit.domain.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MatchDayViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val resourceProvider: ResourceProvider,
  private val fetch: FetchMatchesInfoUseCase,
  private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
  private val fetchLatestMatchThreadsForToday: FetchLatestMatchThreadsForTodayUseCase,
) : MVIViewModel<MatchDayViewEvent, MatchDayViewState, MatchDayViewEffect>() {

  private var savedMatchThreads = emptyList<MatchThreadEntity>()
  private var savedMatches = emptyList<MatchEntity>()

  override fun setInitialState(): MatchDayViewState = MatchDayViewState()

  override fun handleEvent(event: MatchDayViewEvent) {
    super.handleEvent(event)
    when (event) {
      is MatchDayViewEvent.GetLatestMatches -> onGetLatestMatches()
      is MatchDayViewEvent.Refresh -> onRefresh()
      is MatchDayViewEvent.NavigateToMatch -> onNavigateToMatch(event)
      is MatchDayViewEvent.ToggleLiveMode -> filterList(isLiveMode = event.isLiveMode)
    }
  }

  private fun onGetLatestMatches() = viewModelScope.launch {
    if (savedMatches.isEmpty()) {
      setState { copy(matchDayState = MatchDayState.Loading) }
      fetchRedditContent()
      fetchMatchData()
    } else {
      updateMatchSuccessOrEmpty()
    }
  }

  private fun onRefresh() = viewModelScope.launch {
    setState { copy(isRefreshing = true) }
    fetchRedditContent()
    fetchMatchData()
  }

  private fun onNavigateToMatch(
    event: MatchDayViewEvent.NavigateToMatch,
  ) = viewModelScope.launch {
    either {
      createMatchThreadFrom(
        matchId = event.match.id,
        matchThreadList = savedMatchThreads,
        matchList = savedMatches,
      ).bind()
    }.fold(
      { setEffect { MatchDayViewEffect.NavigationError(it.message) } },
      { setEffect { MatchDayViewEffect.NavigateToMatchThread(it.toDestination()) } },
    )
  }

  private suspend fun fetchMatchData() = either {
    fetch.execute(topLeaguesOnly = true).bind()
  }.mapLeft { it.toErrorMsg() }
    .map { it.toMatchEntity() }
    .fold(
      { errorMsg ->
        setState {
          copy(
            matchDayState = MatchDayState.Error(errorMsg),
            isRefreshing = false,
          )
        }
      },
      { matchList ->
        savedMatches = matchList
        updateMatchSuccessOrEmpty()
      },
    )

  private suspend fun fetchRedditContent() = either {
    refreshTokenIfNeeded.execute().bind()
    fetchLatestMatchThreadsForToday.execute().bind()
  }.fold(
    { error ->
      setEffect { MatchDayViewEffect.Error(error.toErrorMsg()) }
    },
    { matchThreads ->
      savedMatchThreads = matchThreads
    },
  )

  private fun filterList(isLiveMode: Boolean = false) {
    setState { copy(isLiveMode = isLiveMode) }
    updateMatchSuccessOrEmpty()
  }

  private fun updateMatchSuccessOrEmpty() {
    val matchList = getValidMatchList(
      matchEntities = savedMatches,
      savedMatchThreads = savedMatchThreads,
      resources = resourceProvider,
      isLiveMode = viewState.value.isLiveMode,
    )
    setState {
      copy(
        matchDayState = if (matchList.isEmpty()) {
          MatchDayState.Empty
        } else {
          MatchDayState.Success(matchList)
        },
        isRefreshing = false,
      )
    }
  }

  private fun DomainError.toErrorMsg(): String {
    Timber.e(this.throwable)
    return when (this) {
      is NetworkError -> resourceProvider.getString(R.string.match_screen_error_no_internet)
      else -> resourceProvider.getString(R.string.match_screen_error_default)
    }
  }
}
