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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun PlaceHolderDivider() {
  Row(Modifier.padding(vertical = 16.dp)) {
    Box(
      modifier = Modifier
        .size(30.dp)
        .liveMatchPlaceHolder(),
    )
    Spacer(
      modifier =
      Modifier
        .size(4.dp),
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
        modifier = Modifier.padding(end = 4.dp),
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
      .size(4.dp),
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
    modifier,
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

private fun Modifier.liveMatchPlaceHolder(): Modifier = composed {
  this
    .placeholder(
      visible = true,
      color = MaterialTheme.colors.onSurface,
      highlight = PlaceholderHighlight.shimmer(
        highlightColor = Color.White.copy(alpha = 0.5f),
        progressForMaxAlpha = 0.5f,
      ),
    )
}

@Preview
@Composable
private fun PlaceHolderDividerPreview() = LiveMatchThemePreview {
  PlaceHolderDivider()
}
