package dev.iurysouza.livematch.matchthread.ui.components.highlights


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.components.thenIf
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.webviewtonativeplayer.NativePlayerEvent
import dev.iurysouza.livematch.webviewtonativeplayer.NativeVideoPlayerView

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HighlightBottomSheet(
  modifier: Modifier,
  mediaItem: MediaItem,
  uiState: HighlightsViewState,
  onDismiss: () -> Unit = {},
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = { onDismiss() },
    sheetState = sheetState,
    tonalElevation = 16.dp,
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    NativeVideoPlayer(
      modifier.padding(horizontal = 16.dp, vertical = 20.dp),
      mediaItem.title, mediaItem.url,
    )
    HighlightsComments(uiState.commentSectionState)
  }
}


@Composable
private fun NativeVideoPlayer(
  modifier: Modifier,
  title: String,
  pageUrl: String,
) {
  var isVideoError by remember { mutableStateOf(false) }
  var isVideoReady by remember { mutableStateOf(false) }
  var isVideoPlaying by remember { mutableStateOf(false) }
  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    VideoTitle(title)
    if (isVideoError) {
      ErrorScreen(
        modifier = Modifier.aspectRatio(16 / 9f, false),
        msg = "Failed to load video",
      )
    } else {
      Box {
        PlaceHolder(isVideoReady = isVideoReady, isVideoError = isVideoError)
        AndroidView(
          factory = {
            NativeVideoPlayerView(
              context = it,
              pageUrl = pageUrl,
              eventListener = { event ->
                when (event) {
                  NativePlayerEvent.Ready -> {
                    isVideoReady = true
                  }

                  is NativePlayerEvent.Error -> {
                    isVideoError = true
                  }

                  NativePlayerEvent.Playing -> {
                    isVideoPlaying = true
                  }
                }
              },
            )
          },
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .thenIf(!isVideoReady) {
              size(0.dp)
            }
            .thenIf(isVideoReady) {
              aspectRatio(16f / 9f)
            },
        )
      }
    }
  }
}

@Composable
private fun VideoTitle(
  title: String,
) {
  Text(
    text = title,
    color = MaterialTheme.colorScheme.onPrimary,
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .padding(bottom = 16.dp),
  )
}

@Composable
private fun PlaceHolder(isVideoReady: Boolean, isVideoError: Boolean) {
  if (!isVideoReady && !isVideoError) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
        .liveMatchPlaceHolder(),
    )
  }
}
