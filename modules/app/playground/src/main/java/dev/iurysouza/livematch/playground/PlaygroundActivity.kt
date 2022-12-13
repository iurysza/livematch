package dev.iurysouza.livematch.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchlist.MatchListScreen
import dev.iurysouza.livematch.matchlist.models.Fakes
import dev.iurysouza.livematch.matchlist.models.MatchListState
import dev.iurysouza.livematch.matchlist.models.MatchListViewState

class PlaygroundActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LivematchTheme {
        MatchListScreen(
          uiState = MatchListViewState(
            matchListState = MatchListState.Success(Fakes.generateMatchList(5)),
            isSyncing = false,
          ),
        )
      }
    }
  }
}
