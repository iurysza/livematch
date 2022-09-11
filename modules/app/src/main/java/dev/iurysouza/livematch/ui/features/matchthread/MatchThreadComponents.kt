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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText

@Composable
fun MatchDescription(
    htmlDescription: String,
) {
    Box(modifier = Modifier.height(400.dp)) {
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
fun CommentList(
    commentList: List<Pair<String, List<CommentItem>>>,
    onClick: (CommentItem) -> Unit,
) {

    val map = mutableMapOf<String, Boolean>()
    commentList.forEach { (group, _) -> map[group] = true }
    var showContent by remember { mutableStateOf(map.toMap()) }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            commentList.forEach { (group: String, comments: List<CommentItem>) ->
                stickyHeader {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showContent = showContent
                                .toMutableMap()
                                .apply { this[group] = !this[group]!! }
                        }
                        .padding(8.dp)) {
                        CommentHeader(group)
                    }
                }
                items(comments) { commentItem: CommentItem ->
                    AnimatedVisibility(visible = shouldShowContent(showContent, group)) {
                        CommentItemComponent(commentItem = commentItem, onClick = onClick)
                    }
                }

            }
        })
}

@Composable
private fun shouldShowContent(
    showContent: Map<String, Boolean>,
    group: String,
) = showContent.isNotEmpty() && showContent[group]!!

@Composable
fun CommentItemComponent(
    commentItem: CommentItem,
    onClick: (CommentItem) -> Unit,
) {


    Column(
        Modifier
            .clickable { onClick(commentItem) }
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp)
        ) {

            Text(text = commentItem.author, fontSize = 12.sp)
            Spacer(Modifier.weight(1f))
            Text(text = commentItem.relativeTime, fontSize = 12.sp)
        }
        RichText(
            Modifier.padding(16.dp)
        ) {
            Markdown(commentItem.body)
        }
    }
}


@Composable
fun CommentProgress() {
    Text(text = "Loading")
}

@Composable
fun CommentHeader(initial: String) {
    Text(
        text = initial,
        fontSize = 24.sp,
        color = Color(0xff039BE5),
        modifier = Modifier
            .background(Color(0xffE3F2FD))
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}
