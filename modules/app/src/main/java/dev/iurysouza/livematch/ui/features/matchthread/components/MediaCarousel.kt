package dev.iurysouza.livematch.ui.features.matchthread.components

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import dev.iurysouza.livematch.ui.features.matchthread.Competition
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.features.matchthread.MatchThreadScreen
import dev.iurysouza.livematch.ui.features.matchthread.MediaItem
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.ui.theme.AppWhite2
import dev.iurysouza.livematch.ui.theme.AppWhite3
import timber.log.Timber

@Composable
fun MediaCarousel(mediaItemList: List<MediaItem>) {
    val context = LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(mediaItemList) { index, item ->
            Box(
                Modifier
                    .clickable { context.launchBrowserTabWith(item.url) }
                    .size(80.dp)
                    .background(AppWhite3, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = item.title,
                    Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 9.sp,
                        color = AppWhite2,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }
    }
}

private fun Context.launchBrowserTabWith(url: String) = runCatching {
    CustomTabsIntent
        .Builder()
        .setUrlBarHidingEnabled(true)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
        .build()
        .launchUrl(this@launchBrowserTabWith, Uri.parse(url))
}.onFailure { Timber.e(it) }

@Composable
fun MatchDetails(content: String, mediaItemList: List<MediaItem>) {
    Column(
        modifier = Modifier
            .background(AppBackgroundColor)
            .padding(horizontal = 8.dp),
    ) {
        RichText(
            modifier = Modifier.padding(8.dp),
            style = RichTextStyle.Default.copy(
                stringStyle = RichTextStringStyle.Default.copy(
                    italicStyle = SpanStyle(
                        fontSize = 12.sp
                    )
                )
            )
        ) {
            Markdown(content)
        }
        MediaCarousel(mediaItemList)
    }

}

@Preview
@Composable
private fun MatchThreadPreview() {
    MatchThreadScreen(
        matchThread = MatchThread(
            competition = Competition(
                emblemUrl = "",
                id = null,
                name = ""
            ),
            content = "Real Madrid",
            id = "id",
            startTime = 9,
            mediaList = emptyList(),
            homeTeam = dev.iurysouza.livematch.ui.features.matchlist.Team(
                crestUrl = null,
                name = "",
                isHomeTeam = false,
                isAhead = false,
                score = ""
            ),
            awayTeam = dev.iurysouza.livematch.ui.features.matchlist.Team(
                crestUrl = null,
                name = "",
                isHomeTeam = false,
                isAhead = false,
                score = ""
            ),
            refereeList = listOf(),
        ),
        navigateUp = {}
    )
}
