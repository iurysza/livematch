package dev.iurysouza.livematch.matchthread.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.matchthread.R

@Composable
fun ScreenToolbar(navigateUp: () -> Unit) {
  Row(
    horizontalArrangement = Arrangement.Start,
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 4.dp)
      .background(Color.Transparent),
  ) {
    IconButton(
      onClick = navigateUp,
    ) {
      Icon(
        imageVector = Icons.Filled.ArrowBack,
        tint = MaterialTheme.colors.onPrimary,
        contentDescription = stringResource(R.string.icon_description),
      )
    }
  }
}
