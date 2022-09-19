package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.theme.ColorPrimary

@Composable
fun MatchThreadScreen(
    matchThread: MatchThread,
    navigateUp: () -> Unit,
) {
    val viewModel = hiltViewModel<MatchThreadViewModel>()
    LaunchedEffect(Unit) {
        viewModel.update(matchThread)
    }

    val commentsState = viewModel.commentsState.collectAsState().value
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_description),
                        )
                    }
                },
                title = { Text(text = stringResource(R.string.match_thread)) },
                backgroundColor = ColorPrimary,
            )
        },
    ) {
        Column {
            when (state) {
                MatchDescriptionState.Loading -> FullScreenProgress()
                is MatchDescriptionState.Error -> ErrorScreen(state.msg)
                is MatchDescriptionState.Success -> MatchDescription(
                    state.matchThread.content,
                    state.matchThread.mediaList,
                )
            }
            when (commentsState) {
                MatchCommentsState.Loading -> FullScreenProgress()
                is MatchCommentsState.Error -> ErrorScreen(commentsState.msg)
                is MatchCommentsState.Success -> {
                    CommentSectionComponent(
                        commentSectionList = commentsState.commentSectionList,
                        onClick = {}
                    )
                }
            }

        }
    }

}


@Preview
@Composable
private fun MatchThreadPreview() {
    MatchThreadScreen(
        matchThread = MatchThread(
            title = "Espanyol vs Real Madrid",
            competition = "LaLiga",
            contentByteArray = "Real Madrid".toByteArray(),
            id = "id",
            startTime = 9,
            mediaList = emptyList(),
        ),
        navigateUp = {}
    )
}
