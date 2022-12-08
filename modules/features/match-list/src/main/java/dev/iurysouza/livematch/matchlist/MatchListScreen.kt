package dev.iurysouza.livematch.matchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.matchlist.betterarchitecture.MatchListViewState

@Composable
fun MatchListScreen(
    uiModel: MatchListViewState,
    onTapItem: (Match) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val refreshState = rememberPullRefreshState(uiModel.isRefreshing, onRefresh = onRefresh)

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxHeight()
            .pullRefresh(refreshState),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.matches),
                        color = MaterialTheme.colors.onPrimary)
                },
                backgroundColor = MaterialTheme.colors.background,
                actions = {
                    if (uiModel.isSyncing) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = stringResource(R.string.icon_description),
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxHeight(),
            ) {
                when (uiModel.matchListState) {
                    is MatchListState.Error -> ErrorScreen(uiModel.matchListState.msg)
                    MatchListState.Loading -> FullScreenProgress()
                    is MatchListState.Success -> MatchesList(
                        modifier = Modifier,
                        matchItemList = uiModel.matchListState.matches,
                        onTapMatchItem = onTapItem,
                    )
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = uiModel.isRefreshing,
                state = refreshState,
            )
        }
    }
}
