package dev.iurysouza.livematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.matchlist.Match
import dev.iurysouza.livematch.matchlist.MatchListScreenComponent
import dev.iurysouza.livematch.matchlist.MatchListState
import dev.iurysouza.livematch.matchlist.Team
import javax.inject.Inject

class PlaygroundActivity : ComponentActivity() {

    @Inject
    lateinit var jsonParser: JsonParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LivematchTheme {
                MatchListScreenComponent(
                    isRefreshing = false,
                    refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { }),
                    state = MatchListState.Success(
                        listOf(
                            Match(
                                id = "",
                                homeTeam = Team(
                                    crestUrl = "https://crests.football-data.org/770.svg",
                                    name = "England",
                                    isHomeTeam = false,
                                    isAhead = true,
                                    score = "1"
                                ),
                                awayTeam = Team(
                                    crestUrl = "https://crests.football-data.org/776.svg",
                                    name = "Wales",
                                    isHomeTeam = false,
                                    isAhead = false,
                                    score = "0"
                                ),
                                startTime = "16:00",
                                elapsedMinutes = "FT"
                            ),
                        )
                    ),
                    onTapItem = {})
            }
        }
    }
}
