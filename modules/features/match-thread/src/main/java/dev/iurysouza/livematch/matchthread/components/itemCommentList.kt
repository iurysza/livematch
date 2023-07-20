package dev.iurysouza.livematch.matchthread.components

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
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.models.CommentSection
import dev.iurysouza.livematch.matchthread.models.Fake
import dev.iurysouza.livematch.matchthread.models.MatchCommentsState
import kotlinx.collections.immutable.ImmutableList

fun LazyListScope.itemCommentList(
  state: MatchCommentsState,
  expandedSectionMap: Map<String, Boolean> = emptyMap(),
  onSectionTapped: (String) -> Unit = {},
  expandCommentsIf: (String) -> Boolean = { true },
  onInit: (Map<String, Boolean>) -> Unit = {},
) {
  when (state) {
    MatchCommentsState.Loading -> item { FullScreenProgress() }
    is MatchCommentsState.Error -> item { ErrorScreen(msg = state.msg, isScrollable = false) }
    is MatchCommentsState.Success -> {
      onInit(initToggledCommentsState(expandedSectionMap, state.sectionList))
      state.sectionList.forEach { (_, event, comments) ->
        stickyHeader {
          SectionHeader(
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
fun ItemCommentListPreview() = LivematchTheme(isPreview = true) {
  LazyColumn(
    modifier = Modifier
      .background(MaterialTheme.colors.background)
      .fillMaxSize(),
    content = {
      itemCommentList(MatchCommentsState.Success(Fake.generateCommentSection()))
    },
  )
}


fun Map<String, Boolean>.toggleValue(
  value: String,
) = plus(value to !getOrDefault(value, true))

fun Map<String, Boolean>.isValueTrueForKey(description: String): Boolean {
  return this[description]?.let { it && this.isNotEmpty() } ?: false
}

private fun initToggledCommentsState(
  showContent: Map<String, Boolean>,
  commentSections: ImmutableList<CommentSection>,
  defaultToggleState: Boolean = true,
): Map<String, Boolean> {
  if (showContent.isNotEmpty()) return showContent

  val sectionToggleMap: MutableMap<String, Boolean> = mutableMapOf()
  commentSections.forEach { (_, event) ->
    sectionToggleMap[event.description] = defaultToggleState
  }

  return sectionToggleMap.toMap()
}

