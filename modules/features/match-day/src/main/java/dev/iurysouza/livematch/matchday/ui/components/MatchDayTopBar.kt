package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.theme.LiveMatchAssets
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Clock
import dev.iurysouza.livematch.matchday.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDayTopBar(
  modifier: Modifier = Modifier,
  isChecked: Boolean = false,
  onToggle: (Boolean) -> Unit = {},
) {
  TopAppBar(
    modifier = modifier,
    title = {
      Text(
        text = stringResource(R.string.app_name),
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
          .padding(start = S300)
          .fillMaxWidth()
          .padding(S200),
        textAlign = TextAlign.Center,
      )
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.background,
    ),
    actions = {
      SwitcherButton(isChecked = isChecked, onToggle)
    },
  )
}

@Preview
@Composable
private fun MatchDayTopBarUncheckedPreview() = LiveMatchThemePreview {
  MatchDayTopBar(isChecked = false)
}

@Preview
@Composable
private fun MatchDayTopBarPreview() = LiveMatchThemePreview {
  MatchDayTopBar(isChecked = true)
}

@Composable
fun SwitcherButton(
  isChecked: Boolean,
  onToggle: (Boolean) -> Unit,
) {
  Row {
    Switch(
      modifier = Modifier.padding(end = S100),
      checked = isChecked,
      onCheckedChange = { onToggle(it) },
      colors = SwitchDefaults.colors(
        uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
        uncheckedTrackColor = MaterialTheme.colorScheme.onPrimary,

        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
        checkedTrackColor = MaterialTheme.colorScheme.tertiary,
        checkedIconColor = MaterialTheme.colorScheme.primary,
      ),
      thumbContent = {
        Icon(
          imageVector = LiveMatchAssets.Clock,
          contentDescription = "Favorite Icon",
        )
      },
    )
  }
}
