package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.iurysouza.livematch.ui.theme.AppAccentColor
import dev.iurysouza.livematch.ui.theme.AppWhite3
import dev.iurysouza.livematch.ui.theme.AppWhite1


@Composable
internal fun MatchesList(
    modifier: Modifier,
    matchItemList: List<Match>,
    onTapMatchItem: (Match) -> Unit,
) {
    LazyColumn {
        itemsIndexed(matchItemList) { _, matchItem ->
            Column(
                modifier
                    .clickable { onTapMatchItem(matchItem) }
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                MatchItem(modifier, matchItem)
            }
        }
    }
}

@Composable
internal fun MatchItem(modifier: Modifier, match: Match) {
    Row {
        MatchTime(
            modifier = modifier.weight(.15f),
            match = match
        )
        Column(
            modifier.weight(.85f),
            verticalArrangement = Arrangement.Center

        ) {
            Team(modifier, match.homeTeam)
            Team(modifier, match.awayTeam)
        }
    }
}

@Composable
fun MatchTime(modifier: Modifier, match: Match) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = match.startTime,
            textAlign = TextAlign.Center,
            color = AppWhite1,
        )
        Text(
            text = match.elapsedMinutes,
            textAlign = TextAlign.Center,
            color = if (match.elapsedMinutes.contains("'")) {
                AppAccentColor
            } else {
                AppWhite1
            },
        )

    }

}

@Composable
internal fun Team(modifier: Modifier, team: Team) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            modifier = modifier
                .size(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(4.dp),
            model = team.crestUrl,
            contentDescription = "${team.name} crest"
        )
        val style = if (team.isAhead) {
            TextStyle(
                fontSize = 19.sp,
                textAlign = TextAlign.Left,
                color = AppWhite1,
            )
        } else {
            TextStyle(
                fontSize = 19.sp,
                textAlign = TextAlign.Left,
                color = AppWhite3,
            )
        }
        Text(
            text = team.name,
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
