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
  data class GetLatestComments(val params: MatchThreadParams) : MatchThreadViewEvent
  data class GetMatchComments(val params: MatchThreadParams) : MatchThreadViewEvent
}

sealed interface MatchDescriptionState {
  data class Success(
    val content: String,
    val mediaList: ImmutableList<MediaItem>,
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

fun MatchThreadArgs.toParams(): MatchThreadParams {
  return MatchThreadParams(
    id = id,
    title = title,
    content = content,
    startTime = startTime,
  )
}

data class MatchThreadParams(
  val id: String,
  val title: String,
  val content: String,
  val startTime: Long,
)
