package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.ui.components.AnimatedCellExpansion
import dev.iurysouza.livematch.ui.features.matchthread.MediaItem

@Composable
fun MatchDescription(
    modifier: Modifier = Modifier,
    description: String,
    mediaList: List<MediaItem>,
) {
    var showContent by remember { mutableStateOf(false) }
    Box(modifier =
    modifier
        .wrapContentHeight()
        .padding(16.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 12.dp,
            ),
            content = {
                item(description) {
                    RichText(
                        style = RichTextStyle.Default.copy(
                            stringStyle = RichTextStringStyle.Default.copy(
                                italicStyle = SpanStyle(
                                    fontSize = 10.sp
                                )
                            )
                        )
                    ) {
                        Markdown(description)
                    }
                }
                if (mediaList.isNotEmpty()) {
                    item("Media") {
                        Text(
                            text = "Highlights",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            modifier = modifier
                                .fillMaxWidth()
                                .clickable { showContent = !showContent }
                        )
                    }
                    items(mediaList) { item ->
                        AnimatedCellExpansion(
                            modifier,
                            showContentIf = { showContent },
                            content = {
                                ItemLink(
                                    title = item.title,
                                    linkUrl = item.url
                                )
                            },
                        )
                    }
                }
            })
    }
}


