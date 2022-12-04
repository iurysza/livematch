package dev.iurysouza.livematch.features.matchlist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.components.shortToast
import kotlinx.coroutines.launch

@Composable
fun MatchListScreen(
    viewModel: MatchListViewModel = hiltViewModel(),
    onOpenMatchThread: (MatchThread) -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

    val isDark = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colors.background
    SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor, !isDark)
    }

    LaunchedEffect(Unit) {
        viewModel.getLatestMatches(false)
        viewModel.events.collect { effect ->
            when (effect) {
                is MatchListEvents.NavigateToMatchThread -> onOpenMatchThread(effect.matchThread)
                is MatchListEvents.NavigationError -> context.shortToast("This Match has not started yet.")
                is MatchListEvents.Error -> context.shortToast(effect.msg)
            }
        }
    }

    val isRefreshing = viewModel.isRefreshingState.collectAsState(false)
    val refreshScope = rememberCoroutineScope()

    val refreshState = rememberPullRefreshState(isRefreshing.value, onRefresh = {
        refreshScope.launch {
            viewModel.getLatestMatches(true)
        }
    })


    MatchListScreenComponent(
        isRefreshing.value,
        refreshState,
        state = viewModel.state.collectAsState().value
    ) { viewModel.navigateToMatch(it) }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MatchListScreenComponent(
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    state: MatchListState,
    onTapItem: (Match) -> Unit = {},
) {
    val isSyncing = derivedStateOf {
        (state as? MatchListState.Success)?.isSyncing ?: false
    }

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
                    if (isSyncing.value) {
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
    ) {
        Box(
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxHeight()
                    .padding(it),
            ) {
                when (state) {
                    is MatchListState.Error -> ErrorScreen(state.msg)
                    MatchListState.Loading -> FullScreenProgress()
                    is MatchListState.Success -> MatchesList(
                        modifier = Modifier,
                        matchItemList = state.matches,
                        onTapMatchItem = onTapItem,
                    )
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isRefreshing,
                state = refreshState,
            )

        }
    }
}
