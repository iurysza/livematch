package dev.iurysouza.livematch.matchthread.ui.components.highlights

import dev.iurysouza.livematch.common.ViewEvent
import dev.iurysouza.livematch.common.ViewSideEffect
import dev.iurysouza.livematch.common.ViewState
import dev.iurysouza.livematch.matchthread.models.CommentItem
import kotlinx.collections.immutable.ImmutableList

sealed interface HighlightsViewEvent : ViewEvent {
  data class GetLatestComments(val id: String) : HighlightsViewEvent
}

data class HighlightsViewState(val state: HighlightsCommentsViewState = HighlightsCommentsViewState.Loading) : ViewState

sealed interface HighlightsCommentsViewState {
  data class Success(val comments: ImmutableList<CommentItem>) : HighlightsCommentsViewState

  object Loading : HighlightsCommentsViewState

  data class Error(val msg: String) : HighlightsCommentsViewState
}

sealed interface HighlightsViewEffect : ViewSideEffect {
  data class Error(val msg: String) : HighlightsViewEffect
}
