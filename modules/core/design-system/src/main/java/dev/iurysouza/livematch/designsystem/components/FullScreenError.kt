package dev.iurysouza.livematch.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    Text(
      text = msg,
      style = MaterialTheme.typography.displaySmall,
      color = MaterialTheme.colorScheme.onPrimary,
    )
  }
}

@Preview
@Composable
private fun ErrorScreenPreview() = LiveMatchThemePreview {
  ErrorScreen()
}
