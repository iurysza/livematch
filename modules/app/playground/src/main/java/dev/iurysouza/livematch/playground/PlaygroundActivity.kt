package dev.iurysouza.livematch.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.matchthread.ui.components.BottomSheet

class PlaygroundActivity : ComponentActivity() {

  private val item = MediaItem(
    title = "New video",
    url = "https://reddit.com/r/soccer/comments/17x48ae/luis_diazs_father_reacting_to_him_scoring_against/",
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LivematchTheme {
        var showBottomSheet: Boolean by remember { mutableStateOf(true) }
//        MatchDayScreen(
//          modifier = Modifier.clickable {
//            showBottomSheet = true
//          },
//          uiState = MatchDayViewState(
//            matchDayState = MatchDayState.Success(Fakes.generateMatchList()),
//            isRefreshing = false,
//          ),
//        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
              showBottomSheet = true
            },
        ) {
          if (showBottomSheet) {
            BottomSheet(
              mediaItem = item,
              onDismiss = {
                showBottomSheet = false
              },
            )
          }
        }
      }
    }
  }
}
