package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.designsystem.theme.LiveMatchAssets
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Clock
import dev.iurysouza.livematch.matchday.R

@Composable
fun MatchDayTopBar(
  modifier: Modifier = Modifier,
  isChecked: Boolean = false,
  onToggle: (Boolean) -> Unit = {},
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .fillMaxWidth()
      .height(56.dp),
  ) {
    Spacer(modifier = Modifier.padding(24.dp))
    Text(
      text = stringResource(R.string.app_name),
      color = MaterialTheme.colorScheme.onPrimary,
      fontSize = 24.sp,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      textAlign = TextAlign.Center,
    )
    SwitcherButton(isChecked = isChecked, onToggle)
  }
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
  Switch(
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
