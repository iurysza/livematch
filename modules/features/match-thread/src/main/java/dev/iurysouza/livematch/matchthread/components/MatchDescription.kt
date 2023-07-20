package dev.iurysouza.livematch.matchthread.components

import androidx.compose.runtime.Composable
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState

@Composable
fun MatchDescription(state: MatchDescriptionState) {
  when (state) {
    MatchDescriptionState.Loading -> FullScreenProgress()
    is MatchDescriptionState.Error -> ErrorScreen(msg = state.msg)
    is MatchDescriptionState.Success -> MatchDetails(state.content, state.mediaList)
  }
}
