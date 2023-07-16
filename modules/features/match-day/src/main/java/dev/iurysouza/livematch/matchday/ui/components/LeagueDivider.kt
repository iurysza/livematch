package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.iurysouza.livematch.matchday.models.Competition

@Composable
fun LeagueDivider(
  competition: Competition,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    AsyncImage(
      modifier = Modifier
        .clip(CircleShape)
        .size(16.dp),
      model = competition.emblemUrl,
      contentDescription = "${competition.name} emblem",
    )
    Text(
      text = competition.name.uppercase(),
      modifier = Modifier
        .wrapContentWidth(Alignment.Start)
        .padding(horizontal = 8.dp, vertical = 16.dp),
      style = MaterialTheme.typography.subtitle1.copy(color = Color(0xFF7B7B8A)),
    )
  }
}

@Preview
@Composable
fun LeagueDividerPreview() {
  LeagueDivider(
    competition = Competition(
      id = 1,
      name = "Premier League",
      emblemUrl = "https://crests.football-data.org/2051.png",
    ),
  )
}
