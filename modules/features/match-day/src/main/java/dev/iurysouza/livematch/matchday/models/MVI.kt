package dev.iurysouza.livematch.matchday.models

import android.os.Parcelable
import dev.iurysouza.livematch.common.storage.ViewEvent
import dev.iurysouza.livematch.common.storage.ViewSideEffect
import dev.iurysouza.livematch.common.storage.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchDayViewState(
  val matchListState: MatchListState = MatchListState.Loading,
  val isSyncing: Boolean = false,
  val isRefreshing: Boolean = false,
) : ViewState, Parcelable

@Parcelize
sealed interface MatchListState : Parcelable {
  @Parcelize
  data class Success(val matches: List<MatchUiModel>) : MatchListState

  @Parcelize
  object Loading : MatchListState

  @Parcelize
  data class Error(val msg: String) : MatchListState
}

sealed interface MatchDayViewEffect : ViewSideEffect {
  data class Error(val msg: String) : MatchDayViewEffect
  data class NavigationError(val msg: String) : MatchDayViewEffect
  data class NavigateToMatchThread(val matchThread: MatchThread) : MatchDayViewEffect
}

sealed interface MatchDayViewEvent : ViewEvent {
  object Refresh : MatchDayViewEvent
  object GetLatestMatches : MatchDayViewEvent
  data class NavigateToMatch(val match: MatchUiModel) : MatchDayViewEvent
}
