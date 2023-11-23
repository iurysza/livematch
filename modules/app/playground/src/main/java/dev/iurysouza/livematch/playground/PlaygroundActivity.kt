package dev.iurysouza.livematch.playground

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightBottomSheet
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsCommentsViewState
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightsViewState
import kotlinx.collections.immutable.toImmutableList

class PlaygroundActivity : ComponentActivity() {

  private var item = MediaItem(
    title = "New video",
    url = "https://www.reddit.com/r/soccer/comments/1822bh7/a_better_angle_of_leandro_trossards_insane_skill/ ",
    id = "17z6ud2",
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LivematchTheme {
        var showBottomSheet: Boolean by remember { mutableStateOf(false) }
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
              showBottomSheet = true
            },
        ) {
          Column {
            PasteFromClipboard() {
              item = item.copy(url = it)
            }
            if (showBottomSheet) {
              HighlightBottomSheet(
                modifier = Modifier,
                onDismiss = {},
                mediaItem = item,
                uiState = HighlightsViewState(
                  HighlightsCommentsViewState.Success(
                    FakeFactory.commentSection.flatMap { it.commentList }.toImmutableList(),
                  ),
                ),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun PasteFromClipboard(onTextChange: (String) -> Unit) {
  var text by remember { mutableStateOf("") }

  // Getting the clipboard system service
  val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clipData = clipboardManager.primaryClip

  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text,
      modifier = Modifier
        .wrapContentHeight()
        .padding(16.dp)
        .fillMaxWidth(),
      color = Color.White,
    )
    Button(
      onClick = {
        if (clipData != null) {
          text = clipData.getItemAt(0).text.toString()
          onTextChange(text)
        }
      },
    ) {
      Text("Paste from Clipboard")
    }
  }
}
