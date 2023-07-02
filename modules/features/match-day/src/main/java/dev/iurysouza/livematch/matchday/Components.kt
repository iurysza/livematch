package dev.iurysouza.livematch.matchday

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import dev.iurysouza.livematch.matchday.models.Competition
import dev.iurysouza.livematch.matchday.models.Fakes
import dev.iurysouza.livematch.matchday.models.MatchUiModel
import dev.iurysouza.livematch.matchday.models.Team
import kotlin.random.Random

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
internal fun MatchDayGroupedByLeague(
  modifier: Modifier = Modifier,
  matchItemList: List<MatchUiModel> = Fakes.generateMatchList(10),
  onTapMatchItem: (MatchUiModel) -> Unit = {},
) {
  LazyColumn(
    modifier = modifier
      .fillMaxHeight()
      .padding(horizontal = 16.dp),
  ) {
    matchItemList.groupBy { it.competition }.forEach { (competition, matchItemList) ->
      stickyHeader {
        LeagueDivider(modifier, competition)
      }
      itemsIndexed(matchItemList) { _, matchItem ->
        Column(
          modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onTapMatchItem(matchItem) }
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        ) {
          MatchItem(modifier, matchItem)
        }
      }
    }
  }
}

@Composable
fun LeagueDivider(modifier: Modifier, competition: Competition) {
  Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    AsyncImage(
      modifier = modifier
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

@Composable
internal fun MatchesList(
  modifier: Modifier,
  matchItemList: List<MatchUiModel>,
  onTapMatchItem: (MatchUiModel) -> Unit,
) {
  LazyColumn {
    itemsIndexed(matchItemList) { _, matchItem ->
      Column(
        modifier
          .clickable { onTapMatchItem(matchItem) }
          .padding(vertical = 8.dp, horizontal = 16.dp)
          .fillMaxWidth(),
      ) {
        MatchItem(modifier, matchItem)
      }
    }
  }
}

@Composable
internal fun MatchItem(modifier: Modifier, match: MatchUiModel) {
  Row {
    MatchTime(
      modifier = modifier.weight(.15f),
      startTime = match.startTime,
      elapsedMinutes = match.elapsedMinutes,
    )
    Column(
      modifier.weight(.85f),
      verticalArrangement = Arrangement.Center,

      ) {
      Team(modifier, match.homeTeam, match.homeTeam.name)
      Team(modifier, match.awayTeam, match.awayTeam.name)
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
      modifier = modifier
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
      modifier = modifier.weight(.75f),
    )
    Text(
      modifier = modifier.weight(.25F),
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


@Preview
@Composable
fun MatchDayGroupedByLeaguePlaceHolder() {
  val matches = remember { Fakes.generateMatchList(9) }
  val modifier = Modifier
  val placeHolderColor = Color(0xFF7B7B8A)
  val highlight =
    PlaceholderHighlight.shimmer(highlightColor = Color.White.copy(alpha = 0.9f), progressForMaxAlpha = 0.9f)
  LazyColumn(
    modifier = modifier
      .fillMaxHeight()
      .padding(horizontal = 16.dp),
  ) {
    matches.groupBy { it.competition }.forEach { (competition, matchItemList) ->
      stickyHeader {
        LeagueDivider(
          modifier
            .width(100.dp)
            .height(30.dp)
            .placeholder(
              visible = true, color = placeHolderColor,
              highlight = highlight,
            ),
          competition,
        )
      }
      itemsIndexed(matchItemList) { _, matchItem ->
        Column(
          modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        ) {
          Column(
            modifier,
            horizontalAlignment = Alignment.Start,
          ) {
            Text(
              modifier = modifier
                .width(Random.nextInt(48, 200).dp)
                .padding(4.dp)
                .placeholder(
                  visible = true, color = placeHolderColor,
                  highlight = highlight,
                ),
              maxLines = 1,
              text = ".....................",
              textAlign = TextAlign.Center,
            )
            Text(
              modifier = modifier
                .width(Random.nextInt(48, 200).dp)
                .padding(4.dp)
                .placeholder(
                  visible = true, color = placeHolderColor,
                  highlight = highlight,
                ),
              maxLines = 1,
              text = ".....................",
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }
  }
}
