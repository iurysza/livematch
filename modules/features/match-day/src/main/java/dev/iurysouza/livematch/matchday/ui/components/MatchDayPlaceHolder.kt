package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import dev.iurysouza.livematch.matchday.models.Competition
import kotlin.random.Random

@Composable
fun PlaceHolderDivider(
  competition: Competition,
) {
  LeagueDivider(
    competition,
    Modifier
      .width(100.dp)
      .height(30.dp)
      .liveMatchPlaceHolder(),
  )
}

@Composable
fun PlaceHolderItem(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier
      .padding(vertical = 8.dp, horizontal = 16.dp)
      .fillMaxWidth(),
  ) {
    Column(
      Modifier,
      horizontalAlignment = Alignment.Start,
    ) {
      Text(
        modifier = Modifier
          .width(Random.nextInt(48, 200).dp)
          .padding(4.dp)
          .liveMatchPlaceHolder(),
        maxLines = 1,
        text = ".....................",
        textAlign = TextAlign.Center,
      )
      Text(
        modifier = Modifier
          .width(Random.nextInt(48, 200).dp)
          .padding(4.dp)
          .liveMatchPlaceHolder(),
        maxLines = 1,
        text = ".....................",
        textAlign = TextAlign.Center,
      )
    }
  }
}

private fun Modifier.liveMatchPlaceHolder(color: Color = Color(0xFF7B7B8A)): Modifier {
  val highlight =
    PlaceholderHighlight.shimmer(highlightColor = Color.White.copy(alpha = 0.9f), progressForMaxAlpha = 0.9f)
  return this
    .width(100.dp)
    .height(30.dp)
    .placeholder(
      visible = true, color = color,
      highlight = highlight,
    )
}
