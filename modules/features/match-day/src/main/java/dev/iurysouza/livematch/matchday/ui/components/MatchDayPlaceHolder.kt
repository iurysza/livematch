@file:Suppress("MagicNumber")

package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun PlaceHolderDivider(modifier: Modifier = Modifier) {
  Row(modifier.padding(vertical = 16.dp)) {
    Box(
      modifier = Modifier
        .size(30.dp)
        .liveMatchPlaceHolder(),
    )
    Spacer(
      modifier =
      Modifier
        .size(S50),
    )
    Box(
      modifier = Modifier
        .height(30.dp)
        .width(Random.nextInt(50..150).dp)
        .liveMatchPlaceHolder(),
    )
  }
}

@Composable
fun PlaceHolderItem(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier
      .padding(bottom = S100)
      .fillMaxWidth(),
  ) {
    Row(
      horizontalArrangement = Arrangement.SpaceAround,
    ) {
      MatchTimePlaceHolder(
        startTime = "00:00",
        elapsedMinutes = "45'",
        modifier = Modifier.padding(end = S50),
      )
      TeamPlace(Modifier)
    }
  }
}

@Composable
private fun TeamPlace(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.padding(bottom = 2.dp),
    ) {
      TeamPlaceHolder()
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
      modifier = Modifier,
    ) {
      TeamPlaceHolder()
    }
  }
}

@Composable
private fun RowScope.TeamPlaceHolder() {
  Box(
    modifier = Modifier
      .size(21.dp)
      .liveMatchPlaceHolder(),
  )
  Spacer(
    modifier =
    Modifier
      .size(S50),
  )
  Box(
    modifier = Modifier
      .height(21.dp)
      .width(Random.nextInt(50..150).dp)
      .liveMatchPlaceHolder(),
  )
  Spacer(
    modifier = Modifier
      .size(21.dp)
      .weight(.1f),
  )
  Box(
    modifier = Modifier
      .size(21.dp)
      .liveMatchPlaceHolder(),
  )
}

@Preview
@Composable
fun MatchTimePlaceHolderPreview() = LiveMatchThemePreview {
  MatchTimePlaceHolder(
    startTime = "00:00",
    elapsedMinutes = "00'",
  )
}

@Composable
fun MatchTimePlaceHolder(
  startTime: String,
  elapsedMinutes: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier.padding(start = S100),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier
        .padding(bottom = 2.dp)
        .liveMatchPlaceHolder(),

      text = startTime,
      textAlign = TextAlign.Center,
    )
    Text(
      modifier = Modifier.liveMatchPlaceHolder(),
      text = elapsedMinutes,
      textAlign = TextAlign.Center,
    )
  }
}

@Preview
@Composable
private fun PlaceHolderItemPreview() = LiveMatchThemePreview {
  PlaceHolderItem()
}

@Preview
@Composable
private fun TeamPlaceHolderPreview() = LiveMatchThemePreview {
  Row {
    TeamPlaceHolder()
  }
}

@Preview
@Composable
private fun PlaceHolderDividerPreview() = LiveMatchThemePreview {
  PlaceHolderDivider()
}
