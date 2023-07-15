package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.matchday.R

@Composable
fun MatchDayTopBar(
  isSyncing: Boolean,
  modifier: Modifier = Modifier,
) {
  TopAppBar(
    elevation = 0.dp,
    title = {
      Text(
        text = stringResource(R.string.app_name),
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        textAlign = TextAlign.Center,
      )
    },
    backgroundColor = MaterialTheme.colors.background,
    actions = {
      if (isSyncing) {
        IconButton(onClick = {}) {
          Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = stringResource(R.string.icon_description),
          )
        }
      }
    },
  )
}
