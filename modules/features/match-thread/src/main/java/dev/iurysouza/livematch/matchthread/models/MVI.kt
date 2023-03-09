package dev.iurysouza.livematch.matchthread.models

import android.os.Parcelable
import dev.iurysouza.livematch.common.storage.ViewEvent
import dev.iurysouza.livematch.common.storage.ViewSideEffect
import dev.iurysouza.livematch.common.storage.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchThreadViewState(
  val descriptionState: MatchDescriptionState = MatchDescriptionState.Loading,
  val commentSectionState: MatchCommentsState = MatchCommentsState.Loading,
  val isRefreshing: Boolean = false,
) : ViewState, Parcelable

sealed interface MatchThreadViewEvent : ViewEvent {
  data class GetLatestComments(val match: MatchThread) : MatchThreadViewEvent
  data class GetMatchComments(val match: MatchThread) : MatchThreadViewEvent
}

@Parcelize
sealed interface MatchDescriptionState : Parcelable {
  @Parcelize
  data class Success(
    val matchThread: MatchThread,
    val matchEvents: List<MatchEvent>,
  ) : MatchDescriptionState

  @Parcelize
  object Loading : MatchDescriptionState

  @Parcelize
  data class Error(val msg: String) : MatchDescriptionState
}

sealed interface MatchCommentsState : Parcelable {
  @Parcelize
  data class Success(val commentSectionList: List<CommentSection>) : MatchCommentsState

  @Parcelize
  object Loading : MatchCommentsState

  @Parcelize
  data class Error(val msg: String) : MatchCommentsState
}

sealed interface MatchThreadViewEffect : ViewSideEffect {
  data class Error(val msg: String) : MatchThreadViewEffect

}
