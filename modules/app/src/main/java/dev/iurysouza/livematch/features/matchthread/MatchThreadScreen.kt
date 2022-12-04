package dev.iurysouza.livematch.features.matchthread

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.AnimatedCellExpansion
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.features.matchthread.components.CommentItemComponent
import dev.iurysouza.livematch.features.matchthread.components.MatchDetails
import dev.iurysouza.livematch.features.matchthread.components.MatchHeader
import dev.iurysouza.livematch.features.matchthread.components.SectionHeader
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun MatchThreadScreen(
    matchThread: MatchThread,
    navigateUp: () -> Unit,
) {
    val viewModel = hiltViewModel<MatchThreadViewModel>()
    LaunchedEffect(Unit) {
        viewModel.getLatestComments(matchThread, false)
    }
    val systemUiController = rememberSystemUiController()
    val isDark = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor, !isDark)
    }

    val isRefreshing = viewModel.isRefreshingState.collectAsState(false)
    val commentsState = viewModel.commentsState.collectAsState().value
    val state = viewModel.state.collectAsState().value


    val refreshScope = rememberCoroutineScope()
    val refreshState = rememberPullRefreshState(isRefreshing.value, onRefresh = {
        refreshScope.launch {
            viewModel.getLatestComments(matchThread, true)
        }
    })

    MatchThreadComponent(
        isRefreshing = isRefreshing.value,
        refreshState = refreshState,
        matchThread = matchThread,
        state = state,
        commentsState = commentsState,
        navigateUp = navigateUp,
    )

}

@Composable
fun MatchThreadComponent(
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    matchThread: MatchThread,
    state: MatchDescriptionState,
    commentsState: MatchCommentsState,
    navigateUp: () -> Unit = {},
) {
    val sectionToggleMap = mutableMapOf<String, Boolean>()
    var showContent by remember { mutableStateOf(sectionToggleMap.toMap()) }
    Box(
        modifier = Modifier
            .pullRefresh(refreshState)
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        val scrollState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(top = 42.dp),
            state = scrollState,
            content = {
                item {
                    MatchHeader(
                        homeTeam = matchThread.awayTeam,
                        awayTeam = matchThread.homeTeam,
                    )
                }
                item {
                    when (state) {
                        MatchDescriptionState.Loading -> FullScreenProgress()
                        is MatchDescriptionState.Error -> ErrorScreen(state.msg)
                        is MatchDescriptionState.Success -> MatchDetails(
                            state.matchThread.content!!,
                            state.matchThread.mediaList,
                        )
                    }
                }
                when (commentsState) {
                    MatchCommentsState.Loading -> item { FullScreenProgress() }
                    is MatchCommentsState.Error -> item { ErrorScreen(commentsState.msg) }
                    is MatchCommentsState.Success -> {
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
                                    }
                                )
                            }
                        }
                    }
                }
            })
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState,
        )
    }
    ScreenToolbar(navigateUp)
}

@Composable
private fun ScreenToolbar(navigateUp: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .background(Color.Transparent)
    ) {
        IconButton(
            onClick = navigateUp
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = stringResource(R.string.icon_description),
            )
        }

    }
}

