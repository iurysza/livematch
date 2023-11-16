package dev.iurysouza.livematch.matchthread.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.webviewtonativeplayer.NativeVideoPlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem,
  onDismiss: () -> Unit = {},
) {
  val sheetState = rememberModalBottomSheetState()
  ModalBottomSheet(
    onDismissRequest = { onDismiss() },
    sheetState = sheetState,
    dragHandle = { BottomSheetDefaults.DragHandle() },
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    Column(
      modifier = modifier
        .fillMaxWidth(),
      horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
      Text(
        text = mediaItem.title,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
      )
      AndroidView(
        factory = { NativeVideoPlayerView(mediaItem.url, it) },
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f),
      )
    }
  }
}
