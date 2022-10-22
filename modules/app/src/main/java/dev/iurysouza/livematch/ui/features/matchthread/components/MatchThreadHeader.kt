package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.ui.features.matchthread.MediaItem

@Composable
fun MatchDescription(
    modifier: Modifier = Modifier,
    description: String,
    mediaList: List<MediaItem>,
) {
    Box(modifier =
    modifier
        .wrapContentHeight()
        .padding(16.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                item(description) {
                    RichText() {
                        Markdown(description)
                    }
                }
                if (mediaList.isNotEmpty()) {
                    item("Media") {
                        Text(
                            text = "Highlights:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            modifier = modifier
                                .fillMaxWidth()
                        )
                    }
                    items(mediaList) { item ->
                        ItemLink(
                            title = item.title,
                            linkUrl = item.url
                        )
                    }
                }
            })
    }
}


