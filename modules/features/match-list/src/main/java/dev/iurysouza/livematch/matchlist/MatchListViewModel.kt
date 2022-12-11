package dev.iurysouza.livematch.matchlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.common.storage.BaseViewModel
import dev.iurysouza.livematch.footballdata.domain.FetchMatchesUseCase
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.matchlist.models.MatchListState
import dev.iurysouza.livematch.matchlist.models.MatchListViewEffect
import dev.iurysouza.livematch.matchlist.models.MatchListViewEvent
import dev.iurysouza.livematch.matchlist.models.MatchListViewState
import dev.iurysouza.livematch.matchlist.models.createMatchThreadFrom
import dev.iurysouza.livematch.matchlist.models.toMatchList
import dev.iurysouza.livematch.reddit.domain.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.reddit.domain.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MatchListViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val resourceProvider: ResourceProvider,
  private val fetchMatches: FetchMatchesUseCase,
  private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
  private val fetchLatestMatchThreadsForToday: FetchLatestMatchThreadsForTodayUseCase
) : BaseViewModel<MatchListViewEvent, MatchListViewState, MatchListViewEffect>() {

  private val savedMatchThreads = savedStateHandle.getStateFlow(
    key = KEY_MATCH_THREADS,
    initialValue = emptyList<MatchThreadEntity>()
  )
  private val savedMatches = savedStateHandle.getStateFlow(
    key = KEY_MATCHES,
    initialValue = emptyList<MatchEntity>()
  )

  override fun setInitialState(): MatchListViewState = MatchListViewState()

  override fun handleEvent(event: MatchListViewEvent) {
    when (event) {
      MatchListViewEvent.GetLatestMatches -> onGetLatestMatches()
      MatchListViewEvent.Refresh -> onRefresh()
      is MatchListViewEvent.NavigateToMatch -> onNavigateToMatch(event)
    }
  }

  private fun onGetLatestMatches() {
    setState { copy(matchListState = MatchListState.Loading) }
    viewModelScope.launch {
      val savedMatches = savedMatches.value
      if (savedMatches.isNotEmpty()) {
        setState {
          copy(
            matchListState = MatchListState.Success(
              savedMatches.toMatchList(resourceProvider)
            )
          )
        }
      } else {
        fetchRedditContent()
        fetchMatchData()
      }
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
        setState {
          copy(
            matchListState = MatchListState.Success(
              matchList.toMatchList(resourceProvider)
            ),
            isRefreshing = false
          )
        }
      }
    )

  private suspend fun fetchRedditContent() = either {
    setState { copy(isSyncing = true) }
    refreshTokenIfNeeded().bind()
    fetchLatestMatchThreadsForToday().bind()
  }.fold(
    { error ->
      setEffect { MatchListViewEffect.Error(error.toErrorMsg()) }
      setState { copy(isSyncing = false) }
    },
    { matchThreads ->
      savedStateHandle[KEY_MATCH_THREADS] = matchThreads
      setState { copy(isSyncing = false) }
    }
  )

  private fun onRefresh() = viewModelScope.launch {
    setState { copy(isRefreshing = true) }
    fetchMatchData()
    fetchRedditContent()
  }

  private fun onNavigateToMatch(
    event: MatchListViewEvent.NavigateToMatch,
  ) = viewModelScope.launch {
    either {
      createMatchThreadFrom(
        matchId = event.match.id,
        matchThreadList = savedMatchThreads.value,
        matchList = savedMatches.value,
      ).bind()
    }.fold(
      { setEffect { MatchListViewEffect.NavigationError(it.msg) } },
      { setEffect { MatchListViewEffect.NavigateToMatchThread(it) } }
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
