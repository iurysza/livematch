package dev.iurysouza.livematch.designsystem.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview

@Composable
fun ErrorScreen(
  modifier: Modifier = Modifier,
  msg: String = "Error Screen",
  isScrollable: Boolean = true,
) {
  AppColumn(modifier, isScrollable) {
    Text(msg)
  }
}

@Preview
@Composable
private fun ErrorScreenPreview() = LiveMatchThemePreview {
  ErrorScreen()
}
