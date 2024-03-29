package dev.iurysouza.livematch.matchthread.ui.components.highlights

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.iurysouza.livematch.designsystem.components.horizontalGradient
import dev.iurysouza.livematch.designsystem.components.liveMatchPlaceHolder
import dev.iurysouza.livematch.designsystem.components.roundedClip
import dev.iurysouza.livematch.designsystem.theme.Space.S100
import dev.iurysouza.livematch.designsystem.theme.Space.S1000
import dev.iurysouza.livematch.designsystem.theme.Space.S200
import dev.iurysouza.livematch.designsystem.theme.Space.S300
import dev.iurysouza.livematch.designsystem.theme.Space.S50
import dev.iurysouza.livematch.designsystem.theme.Space.S800
import dev.iurysouza.livematch.matchthread.R
import dev.iurysouza.livematch.matchthread.models.FakeFactory
import dev.iurysouza.livematch.matchthread.models.MediaItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

@Composable
fun HighlightCarousel(
  mediaItemList: ImmutableList<MediaItem>,
  onItemTap: (MediaItem) -> Unit = {},
) {
  if (mediaItemList.isEmpty()) return
  Column(
    Modifier
      .padding(vertical = S200)
      .padding(bottom = S50),
  ) {
    Text(
      modifier = Modifier.padding(bottom = S50, start = S300),
      text = stringResource(R.string.highlights),
      style = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onPrimary),
    )
    GradientPaddedMediaCarousel(mediaItemList, onItemTap)
  }
}

@Preview
@Composable
fun HighlightPlaceHolder() {
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
  onItemTap: (MediaItem) -> Unit = {},
) {
  Box {
    val list = remember(mediaItemList) {
      val fakeItem = listOf(MediaItem("fake", "fake", "fake"))
      (fakeItem + mediaItemList + fakeItem).toImmutableList()
    }
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(S100),
    ) {
      items(list) { item ->
        if (item.title == "fake") {
          Box(Modifier.width(S200))
        } else {
          HighlightCard(
            modifier = Modifier.size(S1000),
            item = item,
            onItemTap = { onItemTap(item) },
          )
        }
      }
    }
    val colorScheme = listOf(
      MaterialTheme.colorScheme.background,
      Color.Transparent,
      Color.Transparent,
    )
    Box(
      Modifier
        .height(S1000)
        .width(S800)
        .align(Alignment.CenterStart)
        .horizontalGradient(colorScheme),
    )
    Box(
      Modifier
        .size(S1000)
        .width(S800)
        .align(Alignment.CenterEnd)
        .horizontalGradient(colorScheme.reversed()),
    )
  }
}

@Composable
private fun HighlightCard(
  modifier: Modifier = Modifier,
  item: MediaItem,
  onItemTap: (MediaItem) -> Unit = {},
) {
  Box(
    modifier
      .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
      .roundedClip()
      .clickable { onItemTap(item) }
      .padding(S100),
  ) {
    Text(
      text = item.title,
      Modifier.align(Alignment.Center),
      style = TextStyle(
        fontSize = 9.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
      ),
    )
  }
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
