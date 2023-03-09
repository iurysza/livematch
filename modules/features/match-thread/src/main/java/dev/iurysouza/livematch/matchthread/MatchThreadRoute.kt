package dev.iurysouza.livematch.matchthread

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import dev.iurysouza.livematch.matchthread.models.MatchThread
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEvent
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState

@SuppressLint("UnrememberedMutableState")
@Composable
fun MatchThreadRoute(
  matchThread: MatchThread,
  viewModel: MatchThreadViewModel = hiltViewModel(),
  navigateUp: () -> Unit,
) {
  LaunchedEffect(Unit) {
    viewModel.handleEvent(MatchThreadViewEvent.GetMatchComments(matchThread))
  }
  val uiModel: MatchThreadViewState by rememberSaveable(viewModel) { viewModel.viewState }
  MatchThreadScreen(
    uiModel = uiModel,
    matchThread = matchThread,
    navigateUp = navigateUp,
    onRefresh = { viewModel.handleEvent(MatchThreadViewEvent.GetLatestComments(matchThread)) },
  )
}


