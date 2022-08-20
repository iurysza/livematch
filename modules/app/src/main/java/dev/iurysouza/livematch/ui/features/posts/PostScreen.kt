package dev.iurysouza.livematch.ui.features.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress

@Composable
fun PostsScreen(openPostDetail: (Post) -> Unit) {
    Posts(
        viewModel = hiltViewModel(),
        openPostDetail
    )
}

@Composable
fun Posts(
    viewModel: PostsViewModel,
    navigateToPostDetail: (Post) -> Unit,
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
                is PostScreenState.Success -> {
                    PostList(
                        postList = state.postList,
                        onClick = { navigateToPostDetail(it) }
                    )
                }
                PostScreenState.Loading -> FullScreenProgress()
                is PostScreenState.Error -> ErrorScreen(state.msg)
            }
        }
    }
}

@Composable
private fun PostList(
    postList: List<Post>,
    onClick: (Post) -> Unit,
) {
    LazyColumn {
        itemsIndexed(postList) { _, post ->
            PostItem(
                title = post.title,
                body = post.body,
                bgColor = post.bgColor
            ) {
                onClick(post)
            }
        }
    }
}

@Preview
@Composable
private fun PostListPreview(
) {
    PostList(postList = listOf(
        Post(
            body = "do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            id = 0,
            userId = 0,
            title = "Lorem ipsum dolor sit amet",
            bgColor = 0xFF33A369
        ),
        Post(
            body = "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            id = 1,
            userId = 2,
            title = "Lorem ipsum dolor sit amet",
            bgColor = 0xFF33A369
        ),
        Post(
            body = "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            id = 3,
            userId = 3,
            title = "Lorem ipsum dolor sit amet",
            bgColor = 0xFF33A369
        ),

        ), onClick = {})
}
