package dev.iurysouza.livematch.matchthread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.components.AnimatedCellExpansion
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.matchthread.components.CommentItemComponent
import dev.iurysouza.livematch.matchthread.components.MatchDetails
import dev.iurysouza.livematch.matchthread.components.MatchHeader
import dev.iurysouza.livematch.matchthread.components.ScreenToolbar
import dev.iurysouza.livematch.matchthread.components.SectionHeader
import dev.iurysouza.livematch.matchthread.models.CommentItem
import dev.iurysouza.livematch.matchthread.models.MatchCommentsStateMVI
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionStateVMI
import dev.iurysouza.livematch.matchthread.models.MatchEvent
import dev.iurysouza.livematch.matchthread.models.MatchThread
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState
import dev.iurysouza.livematch.matchthread.models.Team

@Composable
fun MatchThreadScreen(
  uiModel: MatchThreadViewState,
  matchThread: MatchThread,
  navigateUp: () -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  val sectionToggleMap = mutableMapOf<String, Boolean>()
  var showContent by remember { mutableStateOf(sectionToggleMap.toMap()) }
  val isRefreshing = uiModel.isRefreshing
  val commentsState = uiModel.commentsState
  val state = uiModel.descriptionState

  val refreshState = rememberPullRefreshState(
    refreshing = uiModel.isRefreshing,
    onRefresh = { onRefresh() },
  )

  Box(
    modifier = Modifier
      .pullRefresh(refreshState)
      .background(MaterialTheme.colors.background)
      .fillMaxSize(),
  ) {
    val scrollState = rememberLazyListState()
    LazyColumn(
      modifier = Modifier
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
          MatchHeader(
            homeTeam = homeTeam,
            awayTeam = Team(
              crestUrl = matchThread.awayTeam.crestUrl,
              isHomeTeam = matchThread.awayTeam.isHomeTeam,
              isAhead = matchThread.awayTeam.isAhead,
              name = matchThread.awayTeam.name,
              score = matchThread.awayTeam.score,
            ),
          )
        }
        item {
          when (state) {
            MatchDescriptionStateVMI.Loading -> FullScreenProgress()
            is MatchDescriptionStateVMI.Error -> ErrorScreen(state.msg)
            is MatchDescriptionStateVMI.Success -> MatchDetails(
              state.matchThread.content!!,
              state.matchThread.mediaList,
            )
          }
        }
        when (commentsState) {
          MatchCommentsStateMVI.Loading -> item { FullScreenProgress() }
          is MatchCommentsStateMVI.Error -> item { ErrorScreen(commentsState.msg) }
          is MatchCommentsStateMVI.Success -> {
            if (sectionToggleMap.isEmpty()) {
              commentsState.commentSectionList.forEach { (_, event) ->
                sectionToggleMap[event.description] = true
              }
              showContent = sectionToggleMap.toMap()
            }
            commentsState.commentSectionList.forEach { (_: String, event: MatchEvent, comments: List<CommentItem>) ->
              stickyHeader {
                SectionHeader(
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
    PullRefreshIndicator(
      modifier = Modifier.align(Alignment.TopCenter),
      refreshing = isRefreshing,
      state = refreshState,
    )
  }
  ScreenToolbar(navigateUp)
}


