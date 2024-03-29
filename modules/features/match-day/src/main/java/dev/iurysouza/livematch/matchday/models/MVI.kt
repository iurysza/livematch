package dev.iurysouza.livematch.matchday.models

import dev.iurysouza.livematch.common.ViewEvent
import dev.iurysouza.livematch.common.ViewSideEffect
import dev.iurysouza.livematch.common.ViewState
import dev.iurysouza.livematch.common.navigation.Destination
import kotlinx.collections.immutable.ImmutableList

data class MatchDayViewState(
  val matchDayState: MatchDayState = MatchDayState.Loading,
  val isRefreshing: Boolean = false,
  val isLiveMode: Boolean = false,
) : ViewState

sealed interface MatchDayState {
  data class Success(val matches: ImmutableList<MatchUiModel>) : MatchDayState
  object Empty : MatchDayState
  object Loading : MatchDayState
  data class Error(val msg: String) : MatchDayState
}

sealed interface MatchDayViewEffect : ViewSideEffect {
  data class Error(val msg: String) : MatchDayViewEffect
  data class NavigationError(val msg: String) : MatchDayViewEffect
  data class NavigateToMatchThread(val destination: Destination) : MatchDayViewEffect
}

sealed interface MatchDayViewEvent : ViewEvent {
  object Refresh : MatchDayViewEvent
  data class ToggleLiveMode(val isLiveMode: Boolean) : MatchDayViewEvent
  object GetLatestMatches : MatchDayViewEvent
  data class NavigateToMatch(val match: MatchUiModel) : MatchDayViewEvent
}
