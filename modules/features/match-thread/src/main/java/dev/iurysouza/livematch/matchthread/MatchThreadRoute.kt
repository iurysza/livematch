package dev.iurysouza.livematch.matchthread

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.iurysouza.livematch.designsystem.components.shortToast
import dev.iurysouza.livematch.matchthread.models.MatchThread
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEffect.Error
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEvent

@Composable
fun MatchThreadRoute(
  matchThread: MatchThread,
  viewModel: MatchThreadViewModel = hiltViewModel(),
  navigateUp: () -> Unit,
) {
  val context = LocalContext.current
  LaunchedEffect(Unit) {
    viewModel.handleEvent(MatchThreadViewEvent.GetMatchComments(matchThread))
    viewModel.effect.collect {
      when (it) {
        is Error -> context.shortToast(it.msg)
      }
    }
  }

  val uiState by rememberSaveable(viewModel) { viewModel.viewState }
  MatchThreadScreen(
    uiState = uiState,
    matchThread = matchThread,
    navigateUp = navigateUp,
    onRefresh = { viewModel.handleEvent(MatchThreadViewEvent.GetLatestComments(matchThread)) },
  )
}
