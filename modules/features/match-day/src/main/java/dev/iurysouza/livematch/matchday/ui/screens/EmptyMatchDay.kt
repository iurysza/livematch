package dev.iurysouza.livematch.matchday.ui.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.components.AppColumn
import dev.iurysouza.livematch.matchday.R

@Composable
fun EmptyMatchDay() {
  AppColumn() {
    Text(
      text = stringResource(R.string.empty_match_day),
      color = MaterialTheme.colors.onPrimary,
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      textAlign = TextAlign.Center,
    )
  }
}
