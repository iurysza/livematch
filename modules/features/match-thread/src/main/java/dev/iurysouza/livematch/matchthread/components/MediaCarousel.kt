package dev.iurysouza.livematch.matchthread.components

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import dev.iurysouza.livematch.designsystem.theme.LiveMatchThemePreview
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S1000
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MediaItem
import kotlinx.collections.immutable.ImmutableList
import timber.log.Timber

@Composable
fun MatchDetails(
  content: String,
  mediaItemList: ImmutableList<MediaItem>,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.background)
      .padding(horizontal = S100),
  ) {
    MatchDescription(content)
    MediaCarousel(mediaItemList)
  }
}

@Composable
private fun MatchDescription(content: String) {
  RichText(
    modifier = Modifier.padding(S100),
    style = RichTextStyle.Default.copy(
      stringStyle = RichTextStringStyle.Default.copy(
        italicStyle = SpanStyle(
          fontSize = 12.sp,
        ),
      ),
    ),
  ) {
    Markdown(content)
  }
}

@Composable
private fun MediaCarousel(mediaItemList: ImmutableList<MediaItem>) {
  if (mediaItemList.isEmpty()) return
  Column(
    Modifier
      .padding(horizontal = S100)
      .padding(vertical = S200)
      .padding(bottom = S50),
  ) {
    val context = LocalContext.current
    Text(
      modifier = Modifier.padding(bottom = S50),
      text = "Highlights",
      style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onPrimary),
    )
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(S100),
    ) {
      itemsIndexed(mediaItemList) { _, item ->
        Box(
          Modifier
            .size(S1000)
            .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable { context.launchBrowserTabWith(item.url) }
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
    }
  }
}

@Preview
@Composable
fun MatchDetailsPreview() = LiveMatchThemePreview {
  MatchDetails(
    content = FakeFactory.generateMatchDescription,
    mediaItemList = FakeFactory.generateMediaList(),
  )
}

private fun Context.launchBrowserTabWith(url: String) = runCatching {
  CustomTabsIntent
    .Builder()
    .setUrlBarHidingEnabled(true)
    .setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
    .build()
    .launchUrl(this@launchBrowserTabWith, Uri.parse(url))
}.onFailure { Timber.e(it) }
