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

  private val savedMatchThreads = savedStateHandle.getStateFlow(
    key = KEY_MATCH_THREADS,
    initialValue = emptyList<MatchThreadEntity>(),
  )
  private val savedMatches = savedStateHandle.getStateFlow(
    key = KEY_MATCHES,
    initialValue = emptyList<MatchEntity>(),
  )

  override fun setInitialState(): MatchDayViewState = MatchDayViewState()

  override fun handleEvent(event: MatchDayViewEvent) {
    super.handleEvent(event)
    when (event) {
      is MatchDayViewEvent.GetLatestMatches -> onGetLatestMatches()
      is MatchDayViewEvent.Refresh -> onRefresh()
      is MatchDayViewEvent.NavigateToMatch -> onNavigateToMatch(event)
    }
  }

  private fun onGetLatestMatches() = viewModelScope.launch {
    if (savedMatches.value.isEmpty()) {
      setState { copy(matchDayState = MatchDayState.Loading, isSyncing = true) }
      fetchRedditContent()
      fetchMatchData()
    } else {
      updateMatchSuccessOrEmpty(savedMatches.value)
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
        matchThreadList = savedMatchThreads.value,
        matchList = savedMatches.value,
      ).bind()
    }.fold(
      { setEffect { MatchDayViewEffect.NavigationError(it.message) } },
      { setEffect { MatchDayViewEffect.NavigateToMatchThread(it.toDestination()) } },
    )
  }

  private suspend fun fetchMatchData() = either {
    fetch.execute().bind()
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
        savedStateHandle[KEY_MATCHES] = matchList
        updateMatchSuccessOrEmpty(matchList)
      },
    )

  private suspend fun fetchRedditContent() = either {
    refreshTokenIfNeeded.execute().bind()
    fetchLatestMatchThreadsForToday.execute().bind()
  }.fold(
    { error ->
      setEffect { MatchDayViewEffect.Error(error.toErrorMsg()) }
      setState { copy(isSyncing = false) }
    },
    { matchThreads ->
      savedStateHandle[KEY_MATCH_THREADS] = matchThreads
      setState { copy(isSyncing = false) }
    },
  )

  private fun updateMatchSuccessOrEmpty(savedMatches: List<MatchEntity>) {
    val matchList = getValidMatchList(savedMatches, savedMatchThreads.value, resourceProvider)
    setState {
      copy(
        matchDayState = if (matchList.isEmpty()) {
          MatchDayState.Empty
        } else {
          MatchDayState.Success(matchList)
        },
        isRefreshing = false,
        isSyncing = false,
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

private const val KEY_MATCH_THREADS = "matchThreads"
private const val KEY_MATCHES = "matches"
