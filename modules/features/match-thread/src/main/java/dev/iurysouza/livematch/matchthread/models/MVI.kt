package dev.iurysouza.livematch.matchthread.models

import dev.iurysouza.livematch.common.ViewEvent
import dev.iurysouza.livematch.common.ViewSideEffect
import dev.iurysouza.livematch.common.ViewState
import dev.iurysouza.livematch.common.navigation.models.MatchThreadArgs
import kotlinx.collections.immutable.ImmutableList

data class MatchThreadViewState(
  val matchThread: MatchThread? = null,
  val descriptionState: MatchDescriptionState = MatchDescriptionState.Loading,
  val commentSectionState: MatchCommentsState = MatchCommentsState.Loading,
  val isRefreshing: Boolean = false,
) : ViewState

sealed interface MatchThreadViewEvent : ViewEvent {
  data class GetLatestComments(val match: MatchThreadArgs) : MatchThreadViewEvent
  data class GetMatchComments(val match: MatchThreadArgs) : MatchThreadViewEvent
}

sealed interface MatchDescriptionState {
  data class Success(
    val content: String,
    val mediaList: List<MediaItem>,
    val matchEvents: List<MatchEvent>,
  ) : MatchDescriptionState

  object Loading : MatchDescriptionState

  data class Error(val msg: String) : MatchDescriptionState
}

sealed interface MatchCommentsState {
  data class Success(val sectionList: ImmutableList<CommentSection>) : MatchCommentsState

  object Loading : MatchCommentsState

  data class Error(val msg: String) : MatchCommentsState
}

sealed interface MatchThreadViewEffect : ViewSideEffect {
  data class Error(val msg: String) : MatchThreadViewEffect
}
