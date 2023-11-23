package dev.iurysouza.livematch.matchthread.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchStatus
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.matchthread.models.Score
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightCarousel
import dev.iurysouza.livematch.matchthread.ui.components.highlights.HighlightPlaceHolder
import dev.iurysouza.livematch.matchthread.ui.screens.highlights.HighlightBottomSheetScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MatchDetails(
  modifier: Modifier = Modifier,
  content: MatchStatus? = null,
  mediaItemList: ImmutableList<MediaItem> = persistentListOf(),
  isPlaceHolder: Boolean = false,
) {
  var item: MediaItem? by remember { mutableStateOf(null) }
  item?.let {
    HighlightBottomSheetScreen(
      mediaItem = it,
      onDismiss = { item = null },
    )
  }
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.background),
  ) {
    if (isPlaceHolder) {
      MatchDescriptionPlaceHolder()
      HighlightPlaceHolder()
    } else {
      MatchDescription(content, modifier = Modifier.fillMaxWidth())
      HighlightCarousel(
        mediaItemList = mediaItemList,
        onItemTap = { mediaItem ->
          item = mediaItem
        },
      )
    }
  }
}

@Composable
private fun MatchDescriptionPlaceHolder() {
  Column(modifier = Modifier.padding(S200)) {
    Text(
      modifier = Modifier
        .padding(bottom = 4.dp)
        .liveMatchPlaceHolder(500),
      text = "HighlightHighlightssssHighlights",
    )
    Text(
      modifier = Modifier
        .liveMatchPlaceHolder(500),
      text = "HighlightsHighlights",
    )
  }
}

@Preview
@Composable
private fun MatchDescriptionPreview() {
  MatchDescription(content = FakeFactory.matchStatus)
}

@Preview
@Composable
private fun MatchDescriptionPreview2() {
  MatchDescription(content = FakeFactory.emptyMatchStatus)
}

@Composable
private fun MatchDescription(
  content: MatchStatus?,
  modifier: Modifier = Modifier,
) {
  content ?: return
  if (content.homeScore.isEmpty() && content.awayScore.isEmpty()) {
    RichText(
      modifier = modifier
        .padding(horizontal = S200)
        .padding(S100),
      style = RichTextStyle.Default.copy(
        stringStyle = RichTextStringStyle.Default.copy(
          italicStyle = SpanStyle(
            fontSize = 12.sp,
          ),
        ),
      ),
    ) {
      Markdown(content.description)
    }
  } else {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      TeamScore(content.homeScore)
      TeamScore(content.awayScore, true)
    }
  }
}

@Composable
private fun TeamScore(teamA: List<Score>, isReversed: Boolean = false) {
  Column(
    modifier = Modifier
      .padding(horizontal = S200)
      .padding(S100)
      .wrapContentSize(),
  ) {
    teamA.forEach { goal ->
      if (isReversed) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
        ) {
          Text(
            text = goal.player,
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onPrimary,
            ),
          )
          Text(
            modifier = Modifier.padding(start = S100),
            text = "${goal.minute}'",
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onPrimary,
            ),
          )
        }
      } else {
        Row() {
          Text(
            text = "${goal.minute}'",
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onPrimary,
            ),
          )
          Text(
            modifier = Modifier.padding(start = S100),
            text = goal.player,
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onPrimary,
            ),
          )
        }
      }
    }
  }
}


@Preview
@Composable
fun MatchDetailsPreview() = LiveMatchThemePreview {
  MatchDetails(
    content = FakeFactory.matchStatus,
    mediaItemList = FakeFactory.generateMediaList(),
  )
}
