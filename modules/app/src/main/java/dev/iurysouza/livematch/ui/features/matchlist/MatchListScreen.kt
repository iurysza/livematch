package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress

@Composable
fun MatchListScreen(openPostDetail: (Post) -> Unit) {
    Posts(
        viewModel = hiltViewModel(),
        openPostDetail
    )
}

@Composable
fun Posts(
    viewModel: MatchThreadViewModel,
    navigateToMatchThread: (Post) -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.posts)) },
                backgroundColor = Color.White
            )
        }
    ) {
        Column {
            when (state) {
                is MatchListState.Success -> {
                    MatchList(
                        postList = state.matches
                    ) { navigateToMatchThread(it) }
                }
                MatchListState.Loading -> FullScreenProgress()
                is MatchListState.Error -> ErrorScreen(state.msg)
            }
        }
    }
}

@Composable
private fun MatchList(
    postList: List<MatchItem>,
    onClick: (Post) -> Unit,
) {
    LazyColumn {
        itemsIndexed(postList) { _, match ->
            Column(Modifier
                .clickable {
                    onClick(
                        Post(
                            body = match.competition,
                            id = match.id,
                            title = match.title,
                            userId = 0,
                            bgColor = 0
                        )
                    )

                }
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .fillMaxWidth()
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W600,
                            )
                        ) {
                            append(match.title)
                        }
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Left,
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W300,
                            )
                        ) {
                            append(match.competition)
                        }
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Left,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PostListPreview(
) {
    listOf(
        MatchItem(
            title = "Fortuna Dusseldorf vs SSV Jahn Regensburg",
            competition = "German 2. Bundesliga",
        )
    ).let {
        MatchList(
            postList = it
        ) { }
    }
}
