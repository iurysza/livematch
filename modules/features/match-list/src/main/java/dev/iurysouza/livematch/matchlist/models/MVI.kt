package dev.iurysouza.livematch.matchlist.models

import android.os.Parcelable
import dev.iurysouza.livematch.common.storage.ViewEvent
import dev.iurysouza.livematch.common.storage.ViewSideEffect
import dev.iurysouza.livematch.common.storage.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchListViewState(
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

sealed interface MatchListViewEffect : ViewSideEffect {
  data class Error(val msg: String) : MatchListViewEffect
  data class NavigationError(val msg: String) : MatchListViewEffect
  data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListViewEffect
}

sealed interface MatchListViewEvent : ViewEvent {
  object Refresh : MatchListViewEvent
  object GetLatestMatches : MatchListViewEvent
  data class NavigateToMatch(val match: MatchUiModel) : MatchListViewEvent
}
