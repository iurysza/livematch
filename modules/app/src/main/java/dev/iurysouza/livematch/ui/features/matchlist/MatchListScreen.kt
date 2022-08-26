package dev.iurysouza.livematch.ui.features.matchlist

import android.widget.TextView
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.matchlist.MatchThreadEntity
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
                        postList = state.matches,
                        onClick = { navigateToMatchThread(it) }
                    )
                }
                MatchListState.Loading -> FullScreenProgress()
                is MatchListState.Error -> ErrorScreen(state.msg)
            }
        }
    }
}

@Composable
private fun MatchList(
    postList: List<MatchThreadEntity>,
    onClick: (Post) -> Unit,
) {
    LazyColumn {
        itemsIndexed(postList.sortedBy { it.numComments }) { _, match ->
            AndroidView(
                factory = { context ->
                    TextView(context).apply {
                        text =
                            HtmlCompat.fromHtml(match.contentHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onClick(
                            Post(
                                body = match.title,
                                id = match.createdAt.toInt(),
                                title = match.title,
                                userId = match.createdAt.toInt(),
                                bgColor = 0
                            )
                        )
                    }
            )
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
        MatchList(
            postList = it,
            onClick = { }
        )
    }
}
