package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.ui.components.AnimatedCellExpansion
import dev.iurysouza.livematch.ui.features.matchthread.CommentItem
import dev.iurysouza.livematch.ui.features.matchthread.CommentSection
import dev.iurysouza.livematch.ui.features.matchthread.EventIcon
import dev.iurysouza.livematch.ui.features.matchthread.MatchEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentSectionComponent(
    modifier: Modifier = Modifier,
    commentSectionList: List<CommentSection>,
    onClick: (CommentItem) -> Unit,
) {

    val sectionToggleMap = mutableMapOf<String, Boolean>()
    commentSectionList.forEach { (_, event) -> sectionToggleMap[event.description] = true }
    var showContent by remember { mutableStateOf(sectionToggleMap.toMap()) }

    val scrollState = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
        state = scrollState,
        content = {
            commentSectionList.forEach { (sectionName: String, event: MatchEvent, comments: List<CommentItem>) ->
                stickyHeader {
                    SectionHeader(
                        modifier = modifier,
                        event = event,
                        onClick = {
                            showContent = showContent
                                .toMutableMap()
                                .apply { this[event.description] = !this[event.description]!! }
                        },
                    )
                }
                items(comments) { commentItem: CommentItem ->
                    AnimatedCellExpansion(
                        showContentIf = { showContent.isNotEmpty() && showContent[event.description]!! },
                        content = {
                            CommentItemComponent(
                                commentItem = commentItem,
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
        (0..10).forEach { index ->
            if (index % 2 == 0) {
                add(
                    CommentSection(
                        name = "${index + 1}",
                        event = MatchEvent(
                            relativeTime = "${index + 3}'",
                            icon = EventIcon.Goal,
                            description = "Goal! Japan 0, Costa RIca 1, Keysher Fuller (Costa Rica) left footed shot from the centre of the box to the top left corner. Assisted by Yeltsin Tejeda.",
                            keyEvent = true,
                        ),
                        commentList = buildList {
                            (0..7).forEach { index ->
                                add(
                                    CommentItem(
                                        author = "elrubiojefe",
                                        relativeTime = 62 + index,
                                        body = "Fede is just getting better and better. Qatar can't come come soon enough...",
                                        score = "${index + 2}",
                                        flairUrl = "",
                                        flairName = ""
                                    )
                                )
                            }
                        }
                    ))
            } else {
                add(
                    CommentSection(
                        name = "${index + 1}",
                        event = MatchEvent(
                            relativeTime = "${index + 4}'",
                            icon = EventIcon.Substitution,
                            description = "Substitution for Japan.",
                            keyEvent = true,
                        ),
                        commentList = emptyList()
                    )
                )
            }
        }
    }
    CommentSectionComponent(commentSectionList = commentSectionList, onClick = {})
}

