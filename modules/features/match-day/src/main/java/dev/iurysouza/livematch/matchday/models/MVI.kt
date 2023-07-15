package dev.iurysouza.livematch.matchday.models

import dev.iurysouza.livematch.common.ViewEvent
import dev.iurysouza.livematch.common.ViewSideEffect
import dev.iurysouza.livematch.common.ViewState
import dev.iurysouza.livematch.common.navigation.Destination
import kotlinx.collections.immutable.ImmutableList

data class MatchDayViewState(
  val matchListState: MatchListState = MatchListState.Loading,
  val isSyncing: Boolean = false,
  val isRefreshing: Boolean = false,
) : ViewState

sealed interface MatchListState {
  data class Success(val matches: ImmutableList<MatchUiModel>) : MatchListState
  object Empty : MatchListState
  object Loading : MatchListState

  data class Error(val msg: String) : MatchListState
}

sealed interface MatchDayViewEffect : ViewSideEffect {
  data class Error(val msg: String) : MatchDayViewEffect
  data class NavigationError(val msg: String) : MatchDayViewEffect
  data class NavigateToMatchThread(val destination: Destination) : MatchDayViewEffect
}

sealed interface MatchDayViewEvent : ViewEvent {
  object Refresh : MatchDayViewEvent
  object GetLatestMatches : MatchDayViewEvent
  data class NavigateToMatch(val match: MatchUiModel) : MatchDayViewEvent
}
