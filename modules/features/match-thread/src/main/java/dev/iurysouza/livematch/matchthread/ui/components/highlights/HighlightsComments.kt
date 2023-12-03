package dev.iurysouza.livematch.matchthread.ui.components.highlights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.ui.components.comments.CommentItemComponent
import dev.iurysouza.livematch.matchthread.ui.components.comments.CommentItemComponentPlaceHolder
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HighlightsComments(commentSectionState: HighlightsCommentsViewState) {
  val listState = rememberLazyListState()
  LazyColumn(
    state = listState,
    modifier = Modifier
      .background(MaterialTheme.colorScheme.background)
      .fillMaxSize(),
    content = {
      when (commentSectionState) {
        is HighlightsCommentsViewState.Success -> {
          commentSectionState.comments.forEach { comment ->
            item {
              Column {
                CommentItemComponent(comment)
                comment.replies.forEach { nestedComment -> CommentItemComponent(nestedComment) }
              }
            }
          }
        }

        is HighlightsCommentsViewState.Error -> {}
        HighlightsCommentsViewState.Loading -> {
          FakeFactory.commentSection.flatMap { it.commentList }
            .forEach { comment ->
              item { CommentItemComponentPlaceHolder(comment) }
            }
        }
      }
    },
  )
}

@Preview
@Composable
fun HighlightsCommentsPreview() {
  val commentSectionState =
    HighlightsCommentsViewState.Success(FakeFactory.commentSection.flatMap { it.commentList }.toImmutableList())
  HighlightsComments(commentSectionState)
}
