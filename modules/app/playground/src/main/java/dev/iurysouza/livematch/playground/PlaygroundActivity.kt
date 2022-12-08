package dev.iurysouza.livematch.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchlist.Fakes
import dev.iurysouza.livematch.matchlist.MatchListScreen
import dev.iurysouza.livematch.matchlist.MatchListState
import dev.iurysouza.livematch.matchlist.betterarchitecture.MatchListViewState
import javax.inject.Inject

class PlaygroundActivity : ComponentActivity() {

    @Inject
    lateinit var jsonParser: JsonParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LivematchTheme {
                MatchListScreen(
                    uiModel = MatchListViewState(
                        matchListState = MatchListState.Success(
                            Fakes.generateMatchList(5)
                        ),
                        isSyncing = false,
                    )
                )
            }
        }
    }
}
