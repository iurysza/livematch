package dev.iurysouza.livematch.matchthread

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.iurysouza.livematch.common.navigation.models.MatchThreadArgs
import dev.iurysouza.livematch.designsystem.components.shortToast
import dev.iurysouza.livematch.designsystem.theme.SystemColors
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEffect.Error
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEvent
import dev.iurysouza.livematch.matchthread.models.toMatchHeader
import dev.iurysouza.livematch.matchthread.models.toParams
import dev.iurysouza.livematch.matchthread.ui.MatchThreadScreen
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel

@Composable
fun MatchThreadRoute(
  args: MatchThreadArgs,
  viewModel: MatchThreadViewModel = hiltViewModel(),
  onNavigateUp: () -> Unit,
) {
  SystemColors(navigationBarColor = MaterialTheme.colors.background)
  val context = LocalContext.current
  LaunchedEffect(Unit) {
    viewModel.handleEvent(MatchThreadViewEvent.GetMatchComments(args.toParams()))
    viewModel.effect.collect {
      when (it) {
        is Error -> context.shortToast(it.msg)
      }
    }
  }

  val uiState by remember(viewModel.viewState) { viewModel.viewState }
  val matchHeader = remember(Unit) { args.toMatchHeader() }
  MatchThreadScreen(
    matchHeader = matchHeader,
    uiState = uiState,
    onNavigateUp = onNavigateUp,
    onRefresh = { viewModel.handleEvent(MatchThreadViewEvent.GetLatestComments(args.toParams())) },
  )
}
