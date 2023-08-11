package dev.iurysouza.livematch.matchday.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
  Row(
    modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    MatchTime(
      startTime = match.startTime,
      elapsedMinutes = match.elapsedMinutes,
      modifier = Modifier.wrapContentWidth(),
    )
    Column(
      Modifier.fillMaxWidth(),
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
      color = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.align(Alignment.CenterHorizontally),
    )
    Text(
      text = elapsedMinutes,
      textAlign = TextAlign.Center,
      color = if (elapsedMinutes.contains("'")) {
        MaterialTheme.colorScheme.primary
      } else {
        MaterialTheme.colorScheme.onPrimary
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
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
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
      color = MaterialTheme.colorScheme.onPrimary,
    )
  } else {
    TextStyle(
      fontSize = 19.sp,
      textAlign = TextAlign.Left,
      color = MaterialTheme.colorScheme.onSurface,
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
