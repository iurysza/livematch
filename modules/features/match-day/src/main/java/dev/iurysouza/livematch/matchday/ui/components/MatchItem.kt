package dev.iurysouza.livematch.matchday.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.iurysouza.livematch.designsystem.components.roundedClip
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.matchday.models.Competition
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import dev.iurysouza.livematch.matchday.models.Team

@Composable
internal fun MatchItem(
  match: MatchUiModel,
  modifier: Modifier = Modifier,
) {
  Row(modifier) {
    MatchTime(
      startTime = match.startTime,
      elapsedMinutes = match.elapsedMinutes,
      modifier = Modifier.weight(weight = .15f),
    )
    Column(
      Modifier.weight(weight = .85f),
      verticalArrangement = Arrangement.Center,
    ) {
      Team(match.homeTeam)
      Team(match.awayTeam)
    }
  }
}

@Composable
fun MatchTime(
  startTime: String,
  elapsedMinutes: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = startTime,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colors.onPrimary,
    )
    Text(
      text = elapsedMinutes,
      textAlign = TextAlign.Center,
      color = if (elapsedMinutes.contains("'")) {
        MaterialTheme.colors.primary
      } else {
        MaterialTheme.colors.onPrimary
      },
    )
  }
}

@Composable
internal fun Team(
  team: Team,
  modifier: Modifier = Modifier,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier,
  ) {
    AsyncImage(
      modifier = Modifier
        .size(S300)
        .roundedClip()
        .padding(S50),
      model = team.crestUrl,
      contentDescription = "$team.name crest",
    )
    val style = textStyle(team)
    Text(
      text = team.name,
      style = style,
      modifier = Modifier.weight(weight = .75f),
    )
    Text(
      modifier = Modifier.weight(weight = .25F),
      text = team.score,
      style = style,
      textAlign = TextAlign.Right,
    )
  }
}

@Composable
private fun textStyle(team: Team): TextStyle {
  val style = if (team.isAhead) {
    TextStyle(
      fontSize = 19.sp,
      textAlign = TextAlign.Left,
      color = MaterialTheme.colors.onPrimary,
    )
  } else {
    TextStyle(
      fontSize = 19.sp,
      textAlign = TextAlign.Left,
      color = MaterialTheme.colors.onSurface,
    )
  }
  return style
}

@Preview
@Composable
private fun MatchItemPreview() {
  val match = MatchUiModel(
    id = "1",
    homeTeam = Team(
      name = "Arsenal",
      crestUrl = "https://crests.football-data.org/57.svg",
      score = "1",
      isAhead = true,
      isHomeTeam = false,
    ),
    awayTeam = Team(
      name = "Arsenal",
      crestUrl = "https://crests.football-data.org/57.svg",
      score = "1",
      isAhead = true,
      isHomeTeam = false,
    ),
    startTime = "19:00",
    elapsedMinutes = "45'",
    competition = Competition(
      name = "Premier League",
      emblemUrl = "https://crests.football-data.org/57.svg",
      id = 1,
    ),
  )
  MatchItem(match)
}

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun TeamPreview() {
  Team(
    modifier = Modifier,
    team = Team(
      name = "Arsenal",
      crestUrl = "https://crests.football-data.org/57.svg",
      score = "1",
      isAhead = true,
      isHomeTeam = false,
    ),
  )
}
