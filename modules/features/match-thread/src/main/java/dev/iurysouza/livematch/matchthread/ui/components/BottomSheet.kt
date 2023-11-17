package dev.iurysouza.livematch.matchthread.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.thenIf
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.webviewtonativeplayer.NativePlayerListener
import dev.iurysouza.livematch.webviewtonativeplayer.NativeVideoPlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
  modifier: Modifier = Modifier,
  mediaItem: MediaItem,
  onDismiss: () -> Unit = {},
) {
  val sheetState = rememberModalBottomSheetState()
  var isVideoError by remember { mutableStateOf(false) }
  var isVideoReady by remember { mutableStateOf(false) }
  ModalBottomSheet(
    onDismissRequest = { onDismiss() },
    sheetState = sheetState,
    dragHandle = { BottomSheetDefaults.DragHandle() },
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    Box(
      modifier = modifier
        .fillMaxWidth(),
    ) {
      if (isVideoError) {
        ErrorScreen(
          modifier = Modifier.aspectRatio(16 / 9f, false),
          msg = "Failed to load video",
        )
      }
      if (!isVideoReady && !isVideoError) {
        CircularProgressIndicator(
          modifier = Modifier.aspectRatio(16 / 9f, false),
          color = MaterialTheme.colorScheme.primary,
        )
      }
      Column(
        modifier = Modifier
          .thenIf(!isVideoReady || isVideoError) { size(0.dp, 0.dp) }
          .fillMaxWidth(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
      ) {
        Text(
          text = mediaItem.title,
          color = Color.White,
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        )
        AndroidView(
          factory = {
            NativeVideoPlayerView(
              context = it,
              pageUrl = mediaItem.url,
              listener = object : NativePlayerListener {
                override fun onPlayerReady() {
                  isVideoReady = true
                }

                override fun onPlayerError() {
                  isVideoError = true
                }
              },

              )
          },
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        )
      }
    }
  }
}
