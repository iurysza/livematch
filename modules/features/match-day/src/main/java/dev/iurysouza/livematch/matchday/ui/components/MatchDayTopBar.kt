package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S0
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.matchday.R

@Composable
fun MatchDayTopBar(
  modifier: Modifier = Modifier,
  isChecked: Boolean = false,
  onToggle: (Boolean) -> Unit = {},
) {
  TopAppBar(
    modifier = modifier,
    elevation = S0,
    title = {
      Text(
        text = stringResource(R.string.app_name),
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier
          .padding(start = S300)
          .fillMaxWidth()
          .padding(S200),
        textAlign = TextAlign.Center,
      )
    },
    backgroundColor = MaterialTheme.colors.background,
    actions = {
      SwitcherButton(isChecked = isChecked, onToggle)
    },
  )
}

@Preview
@Composable
private fun MatchDayTopBarPreview() = LiveMatchThemePreview {
  MatchDayTopBar()
}

@Composable
fun SwitcherButton(
  isChecked: Boolean,
  onToggle: (Boolean) -> Unit,
) {
  Switch(
    checked = isChecked,
    onCheckedChange = { onToggle(it) },
    colors = SwitchDefaults.colors(
      checkedThumbColor = MaterialTheme.colors.primary,
      uncheckedThumbColor = MaterialTheme.colors.onPrimary,
      uncheckedTrackColor = MaterialTheme.colors.onPrimary,
      checkedTrackColor = MaterialTheme.colors.primary,
    ),
  )
}
