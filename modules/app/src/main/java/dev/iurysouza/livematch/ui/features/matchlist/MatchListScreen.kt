package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.util.shortToast

@Composable
fun MatchListScreen(
    viewModel: MatchListViewModel = hiltViewModel(),
    onOpenMatchThread: (MatchThread) -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, true)
    }

    LaunchedEffect(Unit) {
        viewModel.getLatestMatches()
        viewModel.events.collect { effect ->
            when (effect) {
                is MatchListEvents.NavigateToMatchThread -> onOpenMatchThread(effect.matchThread)
                is MatchListEvents.NavigationError -> context.shortToast(effect.msg.message)
                is MatchListEvents.Error -> context.shortToast(effect.msg)
            }
        }
    }

    MatchListScreenComponent(
        state = viewModel.state.collectAsState().value,
        onTapItem = { viewModel.navigateToMatch(it) }
    )
}

@Composable
fun MatchListScreenComponent(
    state: MatchListState,
    onTapItem: (Match) -> Unit = {},
) {
    val isSyncing = derivedStateOf {
        (state as? MatchListState.Success)?.isSyncing ?: false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.matches)) },
                backgroundColor = Color.White,
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
        Column {
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
    }
}
