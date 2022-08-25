package dev.iurysouza.livematch.ui.features.posts

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.matchlist.MatchThreadEntity
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
    postList: List<MatchThreadEntity>,
    onClick: (Post) -> Unit,
) {
    LazyColumn {
        itemsIndexed(postList) { _, post ->
            RichText(
                modifier = Modifier.padding(16.dp)
            ) {
                Markdown(content = "#### ${post.title} \uD83D\uDFE8")
                val content = post.contentHtml.replace("""<a href="#icon-yellow"></a>""", "\uD83D\uDFE8")
//                val content = post.content.replace("""[](#icon-yellow)""", "\uD83D\uDFE8")
                println(content)
                Markdown(content)
            }
        }
    }
}

@Preview
@Composable
private fun PostListPreview(
) {
    listOf(
        MatchThreadEntity(
            title = "Title",
            content = "Content",
            contentHtml = "ContentHtml",
            createdAt = 8400,
            numComments = 0,
            score = 0,
            url = "Url",
        )
    ).let {
        PostList(
            postList = it,
            onClick = { }
        )
    }
}
