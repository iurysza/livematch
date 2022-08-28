package dev.iurysouza.livematch.ui.features.matchlist

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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
fun MatchListScreen(openPostDetail: (MatchThread) -> Unit) {
    MatchList(
        viewModel = hiltViewModel(),
        openPostDetail
    )
}

@Composable
fun MatchList(
    viewModel: MatchThreadViewModel,
    onNavigateToMatchThread: (MatchThread) -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)
    }
    LaunchedEffect(Unit) { viewModel.getMachList() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                MatchListEffects.Idle -> Log.e("", "Idle")
                is MatchListEffects.NavigateToMatchThread -> onNavigateToMatchThread(effect.matchThread)
                is MatchListEffects.NavigationError -> TODO()
            }
        }
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
                is MatchListState.Error -> ErrorScreen(state.msg)
                MatchListState.Loading -> FullScreenProgress()
                is MatchListState.Success -> MatchList(
                    matchItemList = state.matchList,
                    onClick = { viewModel.navigateTo(it) }
                )
            }
        }
    }
}

@Composable
private fun MatchList(
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
                MatchText(matchItem.title)
                MatchText(matchItem.competition)
            }
        }
    }
}

@Composable
private fun MatchText(text: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.W600,
                )
            ) {
                append(text)
            }
        },
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        fontSize = 19.sp,
        textAlign = TextAlign.Left,
    )
}

@Preview
@Composable
private fun MatchListPreview(
) {
    listOf(
        MatchItem(
            title = "Fortuna Dusseldorf vs SSV Jahn Regensburg",
            competition = "German 2. Bundesliga",
        )
    ).let {
        MatchList(matchItemList = it) { }
    }
}

data class MatchItem(
    val id: String = "",
    val title: String,
    val competition: String,
)
