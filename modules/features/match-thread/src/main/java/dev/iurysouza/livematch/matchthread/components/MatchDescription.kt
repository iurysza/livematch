package dev.iurysouza.livematch.matchthread.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.FullScreenProgress
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState

@Composable
fun MatchDescription(state: MatchDescriptionState) {
  when (state) {
    MatchDescriptionState.Loading -> FullScreenProgress()
    is MatchDescriptionState.Error -> ErrorScreen(msg = state.msg, isScrollable = false)
    is MatchDescriptionState.Success -> MatchDetails(state.content, state.mediaList)
  }
}

@Preview
@Composable
private fun MatchDescriptionPreview() = LiveMatchThemePreview {
  MatchDescription(
    MatchDescriptionState.Success(
      FakeFactory.generateMatchDescription(),
      FakeFactory.generateMediaList(),
    ),
  )
}

@Preview
@Composable
private fun MatchDescriptionErrorPreview() = LiveMatchThemePreview {
  MatchDescription(MatchDescriptionState.Error("Error"))
}

@Preview
@Composable
private fun MatchDescriptionLoadingPreview() = LiveMatchThemePreview {
  MatchDescription(MatchDescriptionState.Loading)
}
