package dev.iurysouza.livematch.matchday

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import dev.iurysouza.livematch.common.navigation.Destination
import dev.iurysouza.livematch.designsystem.components.shortToast
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect.Error
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect.NavigateToMatchThread
import dev.iurysouza.livematch.matchday.models.MatchDayViewEffect.NavigationError
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent.GetLatestMatches
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent.NavigateToMatch
import dev.iurysouza.livematch.matchday.models.MatchDayViewEvent.Refresh
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel

@Composable
fun MatchDayRoute(
  viewModel: MatchDayViewModel = hiltViewModel(),
  onNavigateMatchThread: (Destination) -> Unit = {},
) {
  val context = LocalContext.current
  LaunchedEffect(Unit) {
    viewModel.handleEvent(GetLatestMatches)
    viewModel.effect.collect {
      when (it) {
        is Error -> context.shortToast(it.msg)
        is NavigateToMatchThread -> onNavigateMatchThread(it.destination)
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
