package dev.iurysouza.livematch.matchthread.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState

@Composable
fun MatchDescription(state: MatchDescriptionState) {
  when (state) {
    MatchDescriptionState.Loading -> MatchDetails(isPlaceHolder = true)
    is MatchDescriptionState.Success -> MatchDetails(content = state.score, mediaItemList = state.mediaList)
    is MatchDescriptionState.Error -> ErrorScreen(
      msg = state.msg,
      isScrollable = false,
      modifier = Modifier.height(300.dp),
    )
  }
}

@Preview
@Composable
private fun MatchDescriptionPreview() = LiveMatchThemePreview {
  MatchDescription(
    MatchDescriptionState.Success(
      score = FakeFactory.matchStatus,
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
