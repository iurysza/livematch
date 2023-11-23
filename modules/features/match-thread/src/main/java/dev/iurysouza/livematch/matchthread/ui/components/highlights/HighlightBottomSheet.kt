package dev.iurysouza.livematch.matchthread.ui.components.highlights


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.components.thenIf
import dev.iurysouza.livematch.designsystem.theme.Space.S300
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
  var size by remember { mutableStateOf(28.dp) }

  val animatedSize by animateDpAsState(
    targetValue = size,
    animationSpec = tween(150),
    label = "animatedSize",
  )

  val sheetState = rememberModalBottomSheetState {
    val isExpanded = when (it) {
      SheetValue.Hidden, SheetValue.PartiallyExpanded -> false
      SheetValue.Expanded -> true
    }
    size = if (isExpanded) 0.dp else 28.dp
    true
  }
  ModalBottomSheet(
    sheetState = sheetState,
    shape = RoundedCornerShape(animatedSize),
    containerColor = MaterialTheme.colorScheme.background,
    windowInsets = WindowInsets(
      top = 32.dp,
    ),

    onDismissRequest = { onDismiss() },
  ) {
    NativeVideoPlayer(
      modifier,
      mediaItem.title, mediaItem.url,
    )
    HighlightsComments(uiState.state)
  }
}

@Preview
@Composable
private fun NativeVideoPlayerPreview() {
  NativeVideoPlayer(
    modifier = Modifier,
    title = "North Macedonla [1]-[2] England - Hary Kane 59",
    pageUrl = "https://www.reddit.com/r/soccer/comments/ny6q7q/north_macedonla_12_england_hary_kane_59/",
  )
}

@Composable
private fun NativeVideoPlayer(
  modifier: Modifier,
  title: String,
  pageUrl: String,
) {
  Column(
    modifier = modifier
      .padding(S300)
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    var isVideoError by remember { mutableStateOf(false) }
    VideoTitle(title)
    if (isVideoError) {
      VideoError { isVideoError = false }
    } else {
      var isVideoReady by remember { mutableStateOf(false) }
      Box {
        PlaceHolder(
          isVideoReady = isVideoReady,
          isVideoError = isVideoError,
        )
        if (!isVideoError) {
          AndroidView(
            factory = {
              NativeVideoPlayerView(
                context = it,
                pageUrl = pageUrl,
                eventListener = { event ->
                  when (event) {
                    NativePlayerEvent.Ready -> isVideoReady = true
                    is NativePlayerEvent.Error -> isVideoError = true
                    NativePlayerEvent.Playing -> {
                      /** no-op */
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
}

@Composable
private fun VideoError(modifier: Modifier = Modifier, onRefreshTapped: () -> Unit = {}) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(16f / 9f),
    ) {
    IconButton(
      modifier = Modifier
        .align(Alignment.Center),
      onClick = onRefreshTapped,
    ) {
      Icon(
        modifier = Modifier.size(32.dp),
        tint = MaterialTheme.colorScheme.primary,
        imageVector = Icons.Filled.Refresh,
        contentDescription = "onRefresh",
      )
    }
  }
}

fun splitPlayerAndTime(input: String): String {
  val pattern = Regex("""(.*?)\s*-\s*([^-]*)\s*-\s*(.*)""")
  val result = pattern.find(input) ?: return input
  return "${result.groupValues[1].trim()} - ${result.groupValues[2].trim()}\n${result.groupValues[3].trim()}"
}

@Composable
private fun VideoTitle(
  title: String,
) {
  Text(
    text = splitPlayerAndTime(title),
    color = MaterialTheme.colorScheme.onPrimary,
    textAlign = TextAlign.Center,
    modifier = Modifier
      .fillMaxWidth()
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
