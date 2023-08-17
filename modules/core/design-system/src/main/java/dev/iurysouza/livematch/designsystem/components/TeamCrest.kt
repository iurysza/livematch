package dev.iurysouza.livematch.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun TeamCrest(
  teamCrestUrl: String,
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
  Box {
    Box(
      modifier = modifier
        .size(56.dp)
        .clip(CircleShape)
        .background(backgroundColor),
    )
    AsyncImage(
      modifier = modifier
        .size(56.dp)
        .clip(CircleShape)
        .border(width = 2.dp, backgroundColor, CircleShape),
      contentScale = ContentScale.Crop,
      model = teamCrestUrl,
      contentDescription = "teamCrest",
    )
  }
}
