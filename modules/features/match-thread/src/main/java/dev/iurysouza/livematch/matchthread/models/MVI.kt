package dev.iurysouza.livematch.matchthread.models

import android.os.Parcelable
import dev.iurysouza.livematch.common.storage.ViewEvent
import dev.iurysouza.livematch.common.storage.ViewSideEffect
import dev.iurysouza.livematch.common.storage.ViewState
import dev.iurysouza.livematch.matchthread.CommentSection
import dev.iurysouza.livematch.matchthread.MatchEvent
import dev.iurysouza.livematch.matchthread.MatchThread
import kotlinx.parcelize.Parcelize


@Parcelize
data class MatchThreadViewState(
  val descriptionState: MatchDescriptionStateVMI = MatchDescriptionStateVMI.Loading,
  val commentsState: MatchCommentsStateMVI = MatchCommentsStateMVI.Loading,
  val isRefreshing: Boolean = false,
) : ViewState, Parcelable

sealed interface MatchThreadViewEvent : ViewEvent {

  data class GetLatestComments(val match: MatchThread) : MatchThreadViewEvent
  data class GetMatchComments(val match: MatchThread) : MatchThreadViewEvent

}

@Parcelize
sealed interface MatchDescriptionStateVMI : Parcelable {
  @Parcelize
  data class Success(
    val matchThread: MatchThread,
    val matchEvents: List<MatchEvent>,
  ) : MatchDescriptionStateVMI

  @Parcelize
  object Loading : MatchDescriptionStateVMI

  @Parcelize
  data class Error(val msg: String) : MatchDescriptionStateVMI
}

sealed interface MatchCommentsStateMVI : Parcelable {
  @Parcelize
  data class Success(val commentSectionList: List<CommentSection>) : MatchCommentsStateMVI

  @Parcelize
  object Loading : MatchCommentsStateMVI

  @Parcelize
  data class Error(val msg: String) : MatchCommentsStateMVI
}

sealed interface MatchThreadViewEffect : ViewSideEffect {

}
