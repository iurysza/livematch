package dev.iurysouza.livematch.matchlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.iurysouza.livematch.designsystem.components.shortToast
import dev.iurysouza.livematch.matchlist.models.MatchListViewEffect.Error
import dev.iurysouza.livematch.matchlist.models.MatchListViewEffect.NavigateToMatchThread
import dev.iurysouza.livematch.matchlist.models.MatchListViewEffect.NavigationError
import dev.iurysouza.livematch.matchlist.models.MatchListViewEvent.GetLatestMatches
import dev.iurysouza.livematch.matchlist.models.MatchListViewEvent.NavigateToMatch
import dev.iurysouza.livematch.matchlist.models.MatchListViewEvent.Refresh
import dev.iurysouza.livematch.matchlist.models.MatchThread

@Composable
fun BetterMatchLisRoute(
    viewModel: MatchListViewModel = hiltViewModel(),
    onOpenMatchThread: (MatchThread) -> Unit = {},
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.handleEvent(GetLatestMatches)
        viewModel.effect.collect { event ->
            when (event) {
                is Error -> context.shortToast(event.msg)
                is NavigateToMatchThread -> onOpenMatchThread(event.matchThread)
                is NavigationError -> context.shortToast(event.msg)
            }
        }
    }

    val uiModel by rememberSaveable(viewModel) { viewModel.viewState }
    MatchListScreen(
        uiState = uiModel,
        onTapItem = { viewModel.handleEvent(NavigateToMatch(it)) },
        onRefresh = { viewModel.handleEvent(Refresh) },
    )
}
