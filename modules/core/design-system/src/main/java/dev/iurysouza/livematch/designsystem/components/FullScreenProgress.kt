package dev.iurysouza.livematch.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S400

@Composable
fun FullScreenProgress(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .padding(top = S400)
      .fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
  }
}

@Preview
@Composable
private fun FullScreenProgressPreview() = LiveMatchThemePreview {
  FullScreenProgress()
}
