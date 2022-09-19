package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.R

@Composable
fun MatchDescription(
    htmlDescription: String,
    mediaList: List<MediaItem>,
) {
    Box(modifier =
    Modifier
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
                item(htmlDescription) {
                    RichText() {
                        Markdown(htmlDescription)
                    }
                }
                if (mediaList.isNotEmpty()) {
                    item("Media") {
                        Text(
                            text = "Highlights",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    items(mediaList) { item ->
                        ItemLink(item.title, item.url)
                    }
                }
            })
    }
}

@Composable
private fun ItemLink(title: String, linkUrl: String) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val startIndex = 0
        val endIndex = title.length
        append(title)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )

        // attach a string annotation that stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = linkUrl,
            start = startIndex,
            end = endIndex
        )

    }

// UriHandler parse and opens URI inside AnnotatedString Item in Browse
    val uriHandler = LocalUriHandler.current

// 🔥 Clickable text returns position of text that is clicked in onClick callback
    ClickableText(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentSectionComponent(
    commentSectionList: List<CommentSection>,
    onClick: (CommentItem) -> Unit,
) {

    val map = mutableMapOf<String, Boolean>()
    commentSectionList.forEachIndexed { index, (group, _) -> map[group] = true }
    var showContent by remember { mutableStateOf(map.toMap()) }

    val scrollState = rememberLazyListState()
    LaunchedEffect(Unit) {
        val index = commentSectionList.flatMap { it.commentList }.size - 1
        scrollState.animateScrollToItem(index)
    }

    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            commentSectionList.forEach { (name: String, event: MatchEvent?, comments: List<CommentItem>) ->
                stickyHeader {
                    CommentHeader(
                        initial = name,
                        commentItem = event!!,
                        onClick = {
                            showContent = showContent
                                .toMutableMap()
                                .apply { this[name] = !this[name]!! }
                        })
                }
                itemsIndexed(comments) { index, commentItem: CommentItem ->
                    AnimatedVisibility(
                        visible = showContent.isNotEmpty() && showContent[name]!!,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        CommentItemComponent(
                            commentItem = commentItem,
                            color = if (index % 2 == 0) Color(0xFFE0E0E0) else Color.White,
                            onClick = onClick
                        )
                    }
                }

            }
        })
}

@Composable
fun CommentItemComponent(
    commentItem: CommentItem,
    color: Color,
    onClick: (CommentItem) -> Unit,
) {

    val modifier = Modifier

    Column(
        modifier
            .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .background(color)
            .wrapContentHeight()
    ) {
        Column(modifier = modifier.padding(horizontal = 4.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = commentItem.author,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.points, commentItem.score),
                    fontSize = 12.sp
                )
            }

            RichText() {
                Markdown(commentItem.body)
            }
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(id = R.string.minutes, commentItem.relativeTime),
                    fontSize = 12.sp,
                )
            }
        }
    }
}


@Composable
fun CommentProgress(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CommentHeader(initial: String, commentItem: MatchEvent, onClick: (MatchEvent) -> Unit) {
    Column(
        Modifier
            .clickable {
                onClick(commentItem)
            }
            .background(Color.White)
            .padding(vertical = 8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CircleWith(initial)
            RichText() {
                Text(commentItem.description)
            }
        }
    }
}

@Composable
private fun CircleWith(text: String) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.Black, shape = CircleShape)
            .layout() { measurable, constraints ->
                // Measure the composable
                val placeable = measurable.measure(constraints)

                //get the current max dimension to assign width=height
                val currentHeight = placeable.height
                var heightCircle = currentHeight
                if (placeable.width > heightCircle)
                    heightCircle = placeable.width

                //assign the dimension and the center position
                layout(heightCircle, heightCircle) {
                    // Where the composable gets placed
                    placeable.placeRelative(0, (heightCircle - currentHeight) / 2)
                }
            }) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .padding(4.dp)
                .defaultMinSize(24.dp) //Use a min size for short text.
        )
    }
}
