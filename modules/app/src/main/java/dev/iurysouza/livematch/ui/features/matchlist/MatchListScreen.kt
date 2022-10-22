package dev.iurysouza.livematch.ui.features.matchlist

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.ui.components.ErrorScreen
import dev.iurysouza.livematch.ui.components.FullScreenProgress
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import timber.log.Timber

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
    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)
    }
    LaunchedEffect(Unit) { viewModel.getMachList() }
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { effect ->
            when (effect) {
                MatchListEvents.Idle -> Timber.e("Idle")
                is MatchListEvents.NavigateToMatchThread -> onNavigateToMatchThread(effect.matchThread)
                is MatchListEvents.NavigationError -> Toast.makeText(
                    context,
                    effect.msg.toString(),
                    Toast.LENGTH_SHORT
                ).show()
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
                is MatchListState.Success -> MatchList(
                    matchItemList = state.matchList,
                    onClick = { viewModel.navigateTo(it) }
                )
            }
        }
    }
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
