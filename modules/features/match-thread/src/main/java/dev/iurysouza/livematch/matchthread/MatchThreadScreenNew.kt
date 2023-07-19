package dev.iurysouza.livematch.matchthread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.designsystem.components.LottieAsset
import dev.iurysouza.livematch.designsystem.components.LottiePullToReveal
import dev.iurysouza.livematch.designsystem.theme.Space.S500
import dev.iurysouza.livematch.matchthread.components.MatchDetails
import dev.iurysouza.livematch.matchthread.components.MatchHeaderNew
import dev.iurysouza.livematch.matchthread.components.ScreenToolbar
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState
import dev.iurysouza.livematch.matchthread.models.MatchHeader
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState

@Composable
fun MatchThreadScreenNew(
  uiState: MatchThreadViewState,
  matchHeader: MatchHeader,
  modifier: Modifier = Modifier,
  onNavigateUp: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  val sectionToggleMap = mutableMapOf<String, Boolean>()
  var showContent by remember { mutableStateOf(sectionToggleMap.toMap()) }
  val commentsState = uiState.commentSectionState
  val state = uiState.descriptionState

  LottiePullToReveal(
    modifier = modifier,
    isRefreshing = uiState.isRefreshing,
    onRefresh = onRefresh,
    lottieAsset = LottieAsset.FootballFans,
    content = {
      LazyColumn(
        modifier = Modifier
          .background(MaterialTheme.colors.background)
          .fillMaxSize()
          .padding(top = S500),
        state = rememberLazyListState(),
        content = {
          item { MatchHeaderNew(matchHeader) }
//          when (commentsState) {
//            MatchCommentsState.Loading -> item { FullScreenProgress() }
//            is MatchCommentsState.Error -> item { ErrorScreen(msg = commentsState.msg) }
//            is MatchCommentsState.Success -> {
//              if (sectionToggleMap.isEmpty()) {
//                commentsState.commentSectionList.forEach { (_, event) ->
//                  sectionToggleMap[event.description] = true
//                }
//                showContent = sectionToggleMap.toMap()
//              }
//              commentsState.commentSectionList.forEach {
//                  (
//                    _: String,
//                    event: MatchEvent,
//                    comments: List<CommentItem>,
//                  ),
//                ->
//                stickyHeader {
//                  SectionHeader(
//                    modifier = modifier,
//                    isExpanded = showContent.isNotEmpty() && showContent[event.description]!!,
//                    nestedCommentCount = comments.size,
//                    event = event,
//                    onClick = {
//                      showContent = showContent
//                        .toMutableMap()
//                        .apply {
//                          this[event.description] = !this[event.description]!!
//                        }
//                    },
//                  )
//                }
//                items(comments) { commentItem: CommentItem ->
//                  AnimatedCellExpansion(
//                    showContentIf = { showContent.isNotEmpty() && showContent[event.description]!! },
//                    content = {
//                      CommentItemComponent(commentItem)
//                    },
//                  )
//                }
//              }
//            }
//          }
        },
      )
    },
  )
  ScreenToolbar(onNavigateUp)
}
