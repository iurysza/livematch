package dev.iurysouza.livematch.matchday.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import dev.iurysouza.livematch.matchday.models.Fakes
import kotlin.random.Random

@Preview
@Composable
fun MatchDayPlaceHolder() {
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
