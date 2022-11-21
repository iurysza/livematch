package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.util.shortToast

@Composable
fun MatchListScreen(onOpenMatchThread: (MatchThread) -> Unit) {
    MatchList(
        viewModel = hiltViewModel(),
        onOpenMatchThread
    )
}

@Composable
fun MatchList(
    viewModel: MatchListViewModel,
    onNavigateToMatchThread: (MatchThread) -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, true)
    }

    LaunchedEffect(Unit) { viewModel.getLatestMatches() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { effect ->
            when (effect) {
                is MatchListEvents.NavigateToMatchThread -> onNavigateToMatchThread(effect.matchThread)
                is MatchListEvents.NavigationError -> context.shortToast(effect.msg.message)
                is MatchListEvents.Error -> context.shortToast(effect.msg)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.matches)) },
                backgroundColor = Color.White
            )
        }
    ) {
        Column {
            when (state) {
                is MatchListState.Error -> ErrorScreen(state.msg)
                MatchListState.Loading -> FullScreenProgress()
                is MatchListState.Success -> MatchesList(
                    modifier = Modifier,
                    matchItemList = state.matches,
                    onClick = { viewModel.navigateToMatch(it) })
                else -> {}
            }
        }
    }
}

@Composable
private fun MatchesList(
    modifier: Modifier,
    matchItemList: List<Match>,
    onClick: (Match) -> Unit,
) {
    LazyColumn {
        itemsIndexed(matchItemList) { _, matchItem ->
            Column(
                modifier
                    .clickable { onClick(matchItem) }
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                MatchItem(modifier, matchItem)
            }
        }
    }
}

@Composable
private fun MatchItem(modifier: Modifier, match: Match) {
    Row {
        MatchTime(
            modifier = modifier.weight(.15f),
            match = match
        )
        Column(
            modifier.weight(.85f),
            verticalArrangement = Arrangement.Center

        ) {
            Team(modifier, match.homeTeam)
            Team(modifier, match.awayTeam)
        }
    }
}

@Composable
fun MatchTime(modifier: Modifier, match: Match) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = match.startTime,
            textAlign = TextAlign.Center,
        )
        Text(
            text = match.elapsedMinutes,
            textAlign = TextAlign.Center,
        )

    }

}

@Composable
private fun Team(modifier: Modifier, team: Team) {
    Row(modifier) {
        AsyncImage(
            modifier = modifier
                .size(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(4.dp),
            model = team.crestUrl,
            contentDescription = "${team.name} crest"
        )
        val style = if (team.isAhead) {
            TextStyle(
                fontSize = 19.sp,
                textAlign = TextAlign.Left,
                color = Color.Black,
            )
        } else {
            TextStyle(
                fontSize = 19.sp,
                textAlign = TextAlign.Left,
                color = Color.Gray,
            )
        }
        Text(
            text = team.name,
            style = style,
            modifier = modifier.weight(.75f),
        )
        Text(
            modifier = modifier.weight(.25F),
            text = team.score,
            style = style,
            textAlign = TextAlign.Right,
        )
    }
}
