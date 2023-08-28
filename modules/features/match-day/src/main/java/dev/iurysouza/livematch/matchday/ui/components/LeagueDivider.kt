package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.designsystem.components.TeamCrest
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S300
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
    TeamCrest(
      teamCrestUrl = competition.emblemUrl,
      modifier = Modifier.size(S300),
      backgroundColor = MaterialTheme.colorScheme.onSurface,
    )
    Text(
      text = competition.name.uppercase(),
      modifier = Modifier
        .wrapContentWidth(Alignment.Start)
        .padding(horizontal = S100, vertical = S200),
      style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
    )
  }
}

@Preview
@Composable
private fun LeagueDividerPreview() = LiveMatchThemePreview {
  LeagueDivider(
    competition = Competition(
      id = 1,
      name = "Premier League",
      emblemUrl = "https://crests.football-data.org/2051.png",
    ),
  )
}
