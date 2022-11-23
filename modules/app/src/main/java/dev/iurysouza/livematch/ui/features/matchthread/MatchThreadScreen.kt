package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.string.RichTextStringStyle
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.AnimatedCellExpansion
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.features.matchlist.Team
import dev.iurysouza.livematch.ui.features.matchthread.components.CommentItemComponent
import dev.iurysouza.livematch.ui.features.matchthread.components.MatchHeader
import dev.iurysouza.livematch.ui.features.matchthread.components.SectionHeader
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.ui.theme.TitleColor

@Composable
fun MatchThreadScreen(
    matchThread: MatchThread,
    navigateUp: () -> Unit,
) {
    val viewModel = hiltViewModel<MatchThreadViewModel>()
    LaunchedEffect(Unit) {
        viewModel.update(matchThread)
    }
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(AppBackgroundColor, false)
    }

    val commentsState = viewModel.commentsState.collectAsState().value
    val state = viewModel.state.collectAsState().value

    MatchThreadF(navigateUp, state, matchThread, commentsState)

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchThreadF(
    navigateUp: () -> Unit = {},
    state: MatchDescriptionState,
    matchThread: MatchThread,
    commentsState: MatchCommentsState,
) {
    val sectionToggleMap = mutableMapOf<String, Boolean>()
    var showContent by remember { mutableStateOf(sectionToggleMap.toMap()) }
    Box(
        modifier = Modifier
            .background(AppBackgroundColor)
            .fillMaxSize()
    ) {


        val scrollState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(top = 38.dp),
            state = scrollState,
            content = {
                item {
                    MatchHeader(
                        homeTeam = matchThread.awayTeam,
                        awayTeam = matchThread.homeTeam,
                    )
                }
                item {
                    when (state) {
                        MatchDescriptionState.Loading -> FullScreenProgress()
                        is MatchDescriptionState.Error -> ErrorScreen(state.msg)
                        is MatchDescriptionState.Success -> MatchDetails(
                            state.matchThread.content!!
                        )
                    }
                }
                when (commentsState) {
                    MatchCommentsState.Loading -> item { FullScreenProgress() }
                    is MatchCommentsState.Error -> item { ErrorScreen(commentsState.msg) }
                    is MatchCommentsState.Success -> {
                        if (sectionToggleMap.isEmpty()) {
                            commentsState.commentSectionList.forEach { (_, event) ->
                                sectionToggleMap[event.description] = true
                            }
                            showContent = sectionToggleMap.toMap()
                        }
                        commentsState.commentSectionList.forEach { (sectionName: String, event: MatchEvent, comments: List<CommentItem>) ->
                            val method = if (comments.isEmpty()) {
                                null
                            } else {
                                { _: MatchEvent ->
                                    showContent = showContent
                                        .toMutableMap()
                                        .apply {
                                            this[event.description] = !this[event.description]!!
                                        }
                                }
                            }

                            stickyHeader {
                                SectionHeader(
                                    sectionName = sectionName,
                                    event = event,
                                    onClick = method,
                                )
                            }
                            items(comments) { commentItem: CommentItem ->
                                AnimatedCellExpansion(
                                    showContentIf = { showContent.isNotEmpty() && showContent[event.description]!! },
                                    content = {
                                        CommentItemComponent(
                                            commentItem = commentItem,
                                            onClick = {},
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            })
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        IconButton(
            onClick = navigateUp) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = TitleColor,
                contentDescription = stringResource(R.string.icon_description),
            )
        }

    }
}

@Composable
private fun MatchDetails(content: String) {
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
            homeTeam = Team(
                crestUrl = null,
                name = "",
                isHomeTeam = false,
                isAhead = false,
                score = ""
            ),
            awayTeam = Team(
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
