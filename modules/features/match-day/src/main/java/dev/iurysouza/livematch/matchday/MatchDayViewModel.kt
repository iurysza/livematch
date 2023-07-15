package dev.iurysouza.livematch.matchday

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MVIViewModel
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.footballdata.domain.FetchMatchesUseCase
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent
import dev.iurysouza.livematch.matchday.models.MatchDayViewState
import dev.iurysouza.livematch.matchday.models.MatchListState
import dev.iurysouza.livematch.matchday.models.createMatchThreadFrom
import dev.iurysouza.livematch.matchday.models.toDestination
import dev.iurysouza.livematch.matchday.models.toMatchList
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
  private val fetchMatches: FetchMatchesUseCase,
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
      MatchDayViewEvent.GetLatestMatches -> onGetLatestMatches()
      MatchDayViewEvent.Refresh -> onRefresh()
      is MatchDayViewEvent.NavigateToMatch -> onNavigateToMatch(event)
    }
  }

  private fun onGetLatestMatches() = viewModelScope.launch {
    val savedMatches = savedMatches.value
    if (savedMatches.isEmpty()) {
      setState { copy(matchListState = MatchListState.Loading, isSyncing = true) }
      fetchRedditContent()
      fetchMatchData()
    } else {
      updateMatchSuccessOrEmpty(savedMatches)
    }
  }

  private fun updateMatchSuccessOrEmpty(savedMatches: List<MatchEntity>) {
    val validMatchList = savedMatches.toMatchList(savedMatchThreads.value, resourceProvider)
    setState {
      copy(
        matchListState = if (validMatchList.isEmpty()) {
          MatchListState.Empty
        } else {
          MatchListState.Success(validMatchList)
        },
        isRefreshing = false,
        isSyncing = false,
      )
    }
  }

  private suspend fun fetchMatchData() = either {
    fetchMatches().bind()
  }.mapLeft { it.toErrorMsg() }
    .fold(
      { errorMsg ->
        setState {
          copy(
            matchListState = MatchListState.Error(errorMsg),
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
    refreshTokenIfNeeded().bind()
    fetchLatestMatchThreadsForToday().bind()
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

  private fun onRefresh() = viewModelScope.launch {
    setState { copy(isRefreshing = true) }
    fetchMatchData()
    fetchRedditContent()
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
      { setEffect { MatchDayViewEffect.NavigationError(it.msg) } },
      { setEffect { MatchDayViewEffect.NavigateToMatchThread(it.toDestination()) } },
    )
  }

  private fun DomainError.toErrorMsg(): String = when (this) {
    is NetworkError -> {
      Timber.e(this.message.toString())
      resourceProvider.getString(R.string.match_screen_error_no_internet)
    }

    else -> resourceProvider.getString(R.string.match_screen_error_default)
  }
}

private const val KEY_MATCH_THREADS = "matchThreads"
private const val KEY_MATCHES = "matches"
