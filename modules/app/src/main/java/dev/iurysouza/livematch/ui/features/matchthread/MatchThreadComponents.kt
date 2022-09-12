package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.R

@Composable
fun MatchDescription(
    htmlDescription: String,
) {
    Box(modifier = Modifier.height(200.dp)) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())) {
            RichText(Modifier.padding(16.dp)) {
                Markdown(htmlDescription)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentSectionComponent(
    commentSectionList: List<CommentSection>,
    onClick: (CommentItem) -> Unit,
) {

    val map = mutableMapOf<String, Boolean>()
    commentSectionList.forEachIndexed { index, (group, _) -> map[group] = index == 0 }
    var showContent by remember { mutableStateOf(map.toMap()) }

    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            commentSectionList.forEach { (name: String, leadingComment: CommentItem, comments: List<CommentItem>) ->
                stickyHeader {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showContent = showContent
                                .toMutableMap()
                                .apply { this[name] = !this[name]!! }
                        }
                    ) {
                        CommentHeader(name, leadingComment)
                    }
                }
                itemsIndexed(comments) { index, commentItem: CommentItem ->
                    AnimatedVisibility(
                        visible = showContent.isNotEmpty() && showContent[name]!!
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
            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .background(color)
            .wrapContentHeight()
    ) {
        Column(modifier = modifier
            .padding(horizontal = 4.dp)
        ) {
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
fun CommentProgress() {
    Text(text = "Loading")
}

@Composable
fun CommentHeader(initial: String, commentItem: CommentItem) {
    Column() {
        Row {
            CircleWith(initial)
            CommentItemComponent(
                commentItem = commentItem,
                color = Color(0xff039BE5),
                onClick = {}
            )
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
