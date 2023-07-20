package dev.iurysouza.livematch.matchthread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.components.AnimatedCellExpansion
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.designsystem.components.LottieAsset
import dev.iurysouza.livematch.designsystem.components.LottiePullToReveal
import dev.iurysouza.livematch.matchthread.components.CommentItemComponent
import dev.iurysouza.livematch.matchthread.components.MatchDetails
import dev.iurysouza.livematch.matchthread.components.ScreenToolbar
import dev.iurysouza.livematch.matchthread.components.SectionHeader
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.models.MatchCommentsState
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState
import dev.iurysouza.livematch.matchthread.models.MatchEvent
import dev.iurysouza.livematch.matchthread.models.MatchThread
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState
import dev.iurysouza.livematch.matchthread.models.Team

@Composable
fun MatchThreadScreen(
  uiState: MatchThreadViewState,
  matchThread: MatchThread,
  onNavigateUp: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  val sectionToggleMap = mutableMapOf<String, Boolean>()
  var showContent by remember { mutableStateOf(sectionToggleMap.toMap()) }
  val commentsState = uiState.commentSectionState
  val state = uiState.descriptionState

  val modifier = Modifier
  LottiePullToReveal(
    modifier = modifier,
    isRefreshing = uiState.isRefreshing,
    onRefresh = onRefresh,
    lottieAsset = LottieAsset.FootballFans,
    content = {
      Box(
        modifier = modifier
          .background(MaterialTheme.colors.background)
          .fillMaxSize(),
      ) {
        val scrollState = rememberLazyListState()
        LazyColumn(
          modifier = modifier
            .padding(top = 42.dp),
          state = scrollState,
          content = {
            item {
              val homeTeam = Team(
                crestUrl = matchThread.homeTeam.crestUrl,
                isHomeTeam = matchThread.homeTeam.isHomeTeam,
                isAhead = matchThread.homeTeam.isAhead,
                name = matchThread.homeTeam.name,
                score = matchThread.homeTeam.score,
              )
            }
            item {
              when (state) {
                MatchDescriptionState.Loading -> FullScreenProgress(Modifier)
                is MatchDescriptionState.Error -> ErrorScreen(msg = state.msg)
                is MatchDescriptionState.Success -> MatchDetails(state.content, state.mediaList)
              }
            }
            when (commentsState) {
              MatchCommentsState.Loading -> item { FullScreenProgress(Modifier) }
              is MatchCommentsState.Error -> item { ErrorScreen(msg = commentsState.msg) }
              is MatchCommentsState.Success -> {
                if (sectionToggleMap.isEmpty()) {
                  commentsState.sectionList.forEach { (_, event) ->
                    sectionToggleMap[event.description] = true
                  }
                  showContent = sectionToggleMap.toMap()
                }
                commentsState.sectionList.forEach {
                    (
                      _: String,
                      event: MatchEvent,
                      comments: List<CommentItem>,
                    ),
                  ->
                  stickyHeader {
                    SectionHeader(
                      modifier = modifier,
                      isExpanded = showContent.isNotEmpty() && showContent[event.description]!!,
                      nestedCommentCount = comments.size,
                      event = event,
                      onClick = {
                        showContent = showContent
                          .toMutableMap()
                          .apply {
                            this[event.description] = !this[event.description]!!
                          }
                      },
                    )
                  }
                  items(comments) { commentItem: CommentItem ->
                    AnimatedCellExpansion(
                      showContentIf = { showContent.isNotEmpty() && showContent[event.description]!! },
                      content = {
                        CommentItemComponent(commentItem)
                      },
                    )
                  }
                }
              }
            }
          },
        )
      }
    },
  )
  ScreenToolbar(onNavigateUp)
}
