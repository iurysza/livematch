package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.iurysouza.livematch.ui.components.AnimatedCellExpansion
import dev.iurysouza.livematch.ui.features.matchthread.CommentItem
import dev.iurysouza.livematch.ui.features.matchthread.CommentSection
import dev.iurysouza.livematch.ui.features.matchthread.MatchEvent

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun CommentSectionComponent(
    modifier: Modifier = Modifier,
    commentSectionList: List<CommentSection>,
    onClick: (CommentItem) -> Unit,
) {

    val map = mutableMapOf<String, Boolean>()
    commentSectionList.forEach { (group, _) ->
        map[group] = true
    }
    var showContent by remember { mutableStateOf(map.toMap()) }

    val scrollState = rememberLazyListState()
    LaunchedEffect(Unit) {
        val index = commentSectionList.flatMap { it.commentList }.size - 1
        if (index > 0) scrollState.animateScrollToItem(index)
    }

    LazyColumn(
        state = scrollState,
        content = {
            commentSectionList.forEach { (sectionName: String, event: MatchEvent?, comments: List<CommentItem>) ->
                stickyHeader {
                    SectionHeader(
                        modifier = modifier,
                        sectionName = sectionName,
                        event = event,
                        onClick = {
                            showContent = showContent
                                .toMutableMap()
                                .apply { this[sectionName] = !this[sectionName]!! }
                        },
                    )
                }
                items(comments) { commentItem: CommentItem ->
                    AnimatedCellExpansion(
                        showContentIf = {
                            showContent.isNotEmpty() && showContent[sectionName]!!
                        },
                        content = {
                            CommentItemComponent(
                                commentItem = commentItem,
                                color = Color(0xFFF7F4F4),
                                onClick = onClick,
                            )
                        }
                    )
                }
            }
        })
}


@Preview
@Composable
fun CommentSectionComponentPreview() {
    val commentSectionList = buildList<CommentSection> {
        (0..10).forEach {
            add(CommentSection(
                name = "${it + 1}",
                event = MatchEvent(
                    relativeTime = "Event $it",
                    icon = "https://via.placeholder.com/150",
                    description = "Event $it"

                ),
                commentList = buildList {
                    (0..10).forEach { index ->
                        add(
                            CommentItem(
                                author = "elrubiojefe",
                                body = "Fede is just getting better and better. Qatar can't come come soon enough...",
                                score = "11",
                                relativeTime = 62 + index
                            )
                        )
                    }
                }
            ))
        }
    }
    CommentSectionComponent(commentSectionList = commentSectionList, onClick = {})
}

