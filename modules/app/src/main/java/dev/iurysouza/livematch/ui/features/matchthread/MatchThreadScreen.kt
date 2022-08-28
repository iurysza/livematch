package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.features.matchlist.MatchItem
import dev.iurysouza.livematch.ui.features.matchlist.MatchThread
import dev.iurysouza.livematch.ui.theme.ColorPrimary

@Composable
fun MatchThreadScreen(
    navigateUp: () -> Unit,
    post: MatchThread,
) {
    val viewModel = hiltViewModel<MatchThreadViewModel>()
    LaunchedEffect(Unit) {
        viewModel.fetchComments()
    }

    when (val state = viewModel.state.collectAsState().value) {
        is PostDetailScreenState.Success -> MatchThread(state.author, post, navigateUp)
        PostDetailScreenState.Loading -> FullScreenProgress()
        is PostDetailScreenState.Error -> ErrorScreen(state.msg)
    }
    viewModel.update(post)
}


@Composable
private fun MatchThread(
    user: User,
    post: MatchThread,
    navigateUp: () -> Unit,
) {
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
                title = { Text(text = stringResource(R.string.posts_detail)) },
                backgroundColor = ColorPrimary,
            )
        },
    ) {
        MatchThreadComponent(post, user)
    }
}

@Composable
private fun CommentList(
    matchItemList: List<MatchItem>,
    onClick: (MatchItem) -> Unit,
) {
    LazyColumn {
        itemsIndexed(matchItemList) { _, matchItem ->
            Column(
                Modifier
                    .clickable { onClick(matchItem) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
            ) {

            }
        }
    }
}


@Preview
@Composable
private fun PostDetailPreview() {
    MatchThread(
        user = User(
            id = 0,
            name = "name",
            username = "username",
            email = "name@email.com",
            website = "name.com"
        ),
        post = MatchThread(
            matchDescriptionHtml = "",
            title = "",
            competition = "",
            commentList = listOf()
        )
    ) {}
}
