package dev.iurysouza.livematch.matchday

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.iurysouza.livematch.designsystem.components.shortToast
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect.Error
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect.NavigateToMatchThread
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect.NavigationError
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent.GetLatestMatches
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent.NavigateToMatch
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent.Refresh
import dev.iurysouza.livematch.matchday.models.MatchThread

@Composable
fun MatchLisRoute(
  viewModel: MatchDayViewModel = hiltViewModel(),
  onOpenMatchThread: (MatchThread) -> Unit = {},
) {
  val context = LocalContext.current
  LaunchedEffect(Unit) {
    viewModel.handleEvent(GetLatestMatches)
    viewModel.effect.collect {
      when (it) {
        is Error -> context.shortToast(it.msg)
        is NavigateToMatchThread -> onOpenMatchThread(it.matchThread)
        is NavigationError -> context.shortToast(it.msg)
      }
    }
  }

  val uiState by rememberSaveable(viewModel) { viewModel.viewState }
  MatchDayScreen(
    uiState = uiState,
    onTapItem = { viewModel.handleEvent(NavigateToMatch(it)) },
    onRefresh = { viewModel.handleEvent(Refresh) },
  )
}
