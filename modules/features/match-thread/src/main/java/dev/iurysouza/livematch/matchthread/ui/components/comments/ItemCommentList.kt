package dev.iurysouza.livematch.matchthread.ui.components.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.components.AnimatedCellExpansion
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.models.CommentSection
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchCommentsState
import kotlinx.collections.immutable.ImmutableList

fun LazyListScope.itemCommentList(
  state: MatchCommentsState,
  onToggleStateInit: (Map<String, Boolean>) -> Unit = {},
  expandedSectionMap: Map<String, Boolean> = emptyMap(),
  expandCommentsIf: (String) -> Boolean = { true },
  onSectionTapped: (String) -> Unit = {},
) {
  when (state) {
    MatchCommentsState.Loading -> {
      FakeFactory.commentSection.forEach { (_, _, commentList) ->
        stickyHeader { SectionPlaceHolder() }
        items(commentList) { comment: CommentItem ->
          CommentItemComponentPlaceHolder(comment)
        }
      }
    }
    is MatchCommentsState.Error -> item { ErrorScreen(msg = state.msg, isScrollable = false) }
    is MatchCommentsState.Success -> {
      onToggleStateInit(initToggledCommentsState(expandedSectionMap, state.sectionList))
      state.sectionList.forEach { (_, event, comments) ->
        stickyHeader {
          MatchEventSectionHeader(
            event = event,
            nestedCommentCount = comments.size,
            isExpanded = expandCommentsIf(event.description),
            onClick = { onSectionTapped(it.description) },
          )
        }
        items(comments) { comment: CommentItem ->
          AnimatedCellExpansion(
            showContentIf = { expandCommentsIf(event.description) },
            content = { CommentItemComponent(comment) },
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun ItemCommentListPreview() = LiveMatchThemePreview {
  LazyColumn(
    modifier = Modifier
      .background(MaterialTheme.colors.background)
      .fillMaxSize(),
  ) {
    itemCommentList(MatchCommentsState.Success(FakeFactory.commentSection))
  }
}

fun Map<String, Boolean>.toggleValue(
  value: String,
) = plus(value to !getOrDefault(value, true))

fun Map<String, Boolean>.isValueTrueForKey(description: String): Boolean {
  return this[description]?.let { it && this.isNotEmpty() } ?: false
}

private fun initToggledCommentsState(
  expandedSectionMap: Map<String, Boolean>,
  commentSections: ImmutableList<CommentSection>,
  defaultToggleState: Boolean = true,
): Map<String, Boolean> {
  if (expandedSectionMap.isNotEmpty()) return expandedSectionMap

  val sectionToggleMap: MutableMap<String, Boolean> = mutableMapOf()
  commentSections.forEach { (_, event) ->
    sectionToggleMap[event.description] = defaultToggleState
  }

  return sectionToggleMap.toMap()
}
