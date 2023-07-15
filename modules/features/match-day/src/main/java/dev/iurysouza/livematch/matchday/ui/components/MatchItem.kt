package dev.iurysouza.livematch.matchday.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.iurysouza.livematch.matchday.models.Competition
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import dev.iurysouza.livematch.matchday.models.Team

@Composable
internal fun MatchItem(
  modifier: Modifier = Modifier,
  match: MatchUiModel,
) {
  Row {
    MatchTime(
      modifier = modifier.weight(.15f),
      startTime = match.startTime,
      elapsedMinutes = match.elapsedMinutes,
    )
    Column(
      Modifier.weight(.85f),
      verticalArrangement = Arrangement.Center,

      ) {
      Team(Modifier, match.homeTeam, match.homeTeam.name)
      Team(Modifier, match.awayTeam, match.awayTeam.name)
    }
  }
}

@Composable
fun MatchTime(modifier: Modifier, startTime: String, elapsedMinutes: String) {
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
internal fun Team(modifier: Modifier, team: Team, homeTeamName: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier,
  ) {
    AsyncImage(
      modifier = Modifier
        .size(24.dp)
        .clip(RoundedCornerShape(10.dp))
        .padding(4.dp),
      model = team.crestUrl,
      contentDescription = "$homeTeamName crest",
    )
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
    Text(
      text = homeTeamName,
      style = style,
      modifier = Modifier.weight(.75f),
    )
    Text(
      modifier = Modifier.weight(.25F),
      text = team.score,
      style = style,
      textAlign = TextAlign.Right,
    )
  }
}

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun OtherPreview() {
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
  MatchItem(
    modifier = Modifier,
    match = match,
  )
}

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun TeamPreview() {
  val team = Team(
    name = "Arsenal",
    crestUrl = "https://crests.football-data.org/57.svg",
    score = "1",
    isAhead = true,
    isHomeTeam = false,
  )
  Team(
    modifier = Modifier,
    team = Team(
      name = "Arsenal",
      crestUrl = "https://crests.football-data.org/57.svg",
      score = "1",
      isAhead = true,
      isHomeTeam = false,
    ),
    homeTeamName = team.name,
  )
}


