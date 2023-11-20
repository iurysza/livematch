package dev.iurysouza.livematch.matchthread.ui.screens.highlights

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightBottomSheet
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsViewEvent
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel

@Composable
fun HighlightBottomSheetScreen(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem,
  viewModel: HighlightCommentsViewModel = hiltViewModel(),
  onDismiss: () -> Unit = {},
) {
  BackHandler { onDismiss() }
  val uiState by remember(viewModel.viewState) { viewModel.viewState }
  LaunchedEffect(mediaItem) {
    viewModel.handleEvent(HighlightsViewEvent.GetLatestComments(mediaItem.id))
  }
  HighlightBottomSheet(
    modifier = modifier,
    mediaItem = mediaItem,
    uiState = uiState,
    onDismiss = onDismiss,
  )
}
