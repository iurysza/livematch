package dev.iurysouza.livematch.matchlist

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.designsystem.components.shortToast
import dev.iurysouza.livematch.matchlist.betterarchitecture.MatchListViewState

@Composable
fun MatchListRoute(
    viewModel: MatchListViewModel = hiltViewModel(),
    onOpenMatchThread: (MatchThread) -> Unit = {},
) {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val isUsingDarkTheme = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colors.background

    SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor, darkIcons = !isUsingDarkTheme)
    }

    LaunchedEffect(Unit) {
        viewModel.getLatestMatches(false)
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MatchListEvents.NavigateToMatchThread -> onOpenMatchThread(event.matchThread)
                is MatchListEvents.NavigationError -> context.shortToast(event.msg)
                is MatchListEvents.Error -> context.shortToast(event.msg)
            }
        }
    }

    val uiModel by viewModel.uiModel.collectAsState()

    MatchListScreen(
        uiModel = MatchListViewState(uiModel.matchListState, uiModel.isSyncing),
        onTapItem = { viewModel.navigateToMatch(it) },
        onRefresh = { viewModel.getLatestMatches(true) },
    )
}
