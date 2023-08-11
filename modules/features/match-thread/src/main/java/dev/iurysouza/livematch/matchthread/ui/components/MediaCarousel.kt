package dev.iurysouza.livematch.matchthread.ui.components

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.designsystem.components.horizontalGradient
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.components.roundedClip
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S1000
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.designsystem.theme.Space.S800
import dev.iurysouza.livematch.matchthread.R
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MatchStatus
import dev.iurysouza.livematch.matchthread.models.MediaItem
import dev.iurysouza.livematch.matchthread.models.Score
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

@Composable
fun MatchDetails(
  modifier: Modifier = Modifier,
  content: MatchStatus? = null,
  mediaItemList: ImmutableList<MediaItem> = persistentListOf(),
  isPlaceHolder: Boolean = false,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.background),
  ) {
    if (isPlaceHolder) {
      MatchDescriptionPlaceHolder()
      CarouselPlaceHolder()
    } else {
      MatchDescription(content, modifier = Modifier.fillMaxWidth())
      MediaCarousel(mediaItemList)
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
              color = MaterialTheme.colors.onPrimary,
            ),
          )
          Text(
            modifier = Modifier.padding(start = S100),
            text = "${goal.minute}'",
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary,
            ),
          )
        }
      } else {
        Row(
        ) {
          Text(
            text = "${goal.minute}'",
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary,
            ),
          )
          Text(
            modifier = Modifier.padding(start = S100),
            text = goal.player,
            style = TextStyle(
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary,
            ),
          )
        }
      }
    }
  }
}

@Composable
private fun MediaCarousel(mediaItemList: ImmutableList<MediaItem>) {
  if (mediaItemList.isEmpty()) return
  Column(
    Modifier
      .padding(vertical = S200)
      .padding(bottom = S50),
  ) {
    Text(
      modifier = Modifier.padding(bottom = S50, start = S300),
      text = stringResource(R.string.highlights),
      style = TextStyle(fontSize = 15.sp, color = MaterialTheme.colors.onPrimary),
    )
    GradientPaddedMediaCarousel(mediaItemList)
  }
}

@Preview
@Composable
fun CarouselPlaceHolder() {
  Column(
    Modifier
      .padding(vertical = S200)
      .padding(bottom = S50)
      .padding(horizontal = S200),
  ) {
    Text(
      modifier = Modifier
        .padding(bottom = S50)
        .liveMatchPlaceHolder(500),
      text = "Highlights",
    )
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(S100),
    ) {
      items(FakeFactory.generateMediaList()) { item ->
        Box(
          modifier = Modifier
            .size(S1000)
            .liveMatchPlaceHolder(),
        )
      }
    }
  }
}

@Composable
private fun GradientPaddedMediaCarousel(
  mediaItemList: ImmutableList<MediaItem>,
) {
  val context = LocalContext.current
  Box {
    val list = remember(mediaItemList) {
      val fakeItem = listOf(MediaItem("fake", "fake"))
      (fakeItem + mediaItemList + fakeItem).toImmutableList()
    }
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(S100),
    ) {
      items(list) { item ->
        if (item.title == "fake") {
          Box(Modifier.width(S200))
        } else {
          MediaCard(
            modifier = Modifier.size(S1000),
            context = context,
            item = item,
          )
        }
      }
    }
    val colors = listOf(
      MaterialTheme.colors.background,
      Color.Transparent,
      Color.Transparent,
    )
    Box(
      Modifier
        .height(S1000)
        .width(S800)
        .align(Alignment.CenterStart)
        .horizontalGradient(colors = colors),
    )
    Box(
      Modifier
        .size(S1000)
        .width(S800)
        .align(Alignment.CenterEnd)
        .horizontalGradient(colors = colors.reversed()),
    )
  }
}

@Composable
private fun MediaCard(
  modifier: Modifier = Modifier,
  context: Context,
  item: MediaItem,
) {
  val isDarkTheme = isSystemInDarkTheme()
  Box(
    modifier
      .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(10.dp))
      .roundedClip()
      .clickable { context.launchBrowserTabWith(isDarkTheme, item.url) }
      .padding(S100),
  ) {
    Text(
      text = item.title,
      Modifier.align(Alignment.Center),
      style = TextStyle(
        fontSize = 9.sp,
        color = MaterialTheme.colors.onPrimary,
        fontWeight = FontWeight.Bold,
      ),
    )
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

private fun Context.launchBrowserTabWith(isDarkTheme: Boolean, url: String) = runCatching {
  CustomTabsIntent
    .Builder()
    .setUrlBarHidingEnabled(true)
    .setColorScheme(
      if (isDarkTheme) {
        CustomTabsIntent.COLOR_SCHEME_DARK
      } else {
        CustomTabsIntent.COLOR_SCHEME_LIGHT
      },
    )
    .build()
    .launchUrl(this@launchBrowserTabWith, Uri.parse(url))
}.onFailure { Timber.e(it) }
