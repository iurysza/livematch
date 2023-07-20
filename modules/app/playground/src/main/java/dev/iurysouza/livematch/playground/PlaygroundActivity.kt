package dev.iurysouza.livematch.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchday.models.Fakes
import dev.iurysouza.livematch.matchday.models.MatchDayState
import dev.iurysouza.livematch.matchday.models.MatchDayViewState
import dev.iurysouza.livematch.matchday.ui.screens.MatchDayScreen

class PlaygroundActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LivematchTheme {
        MatchDayScreen(
          uiState = MatchDayViewState(
            matchDayState = MatchDayState.Success(Fakes.generateMatchList()),
            isSyncing = false,
          ),
        )
      }
    }
  }
}
