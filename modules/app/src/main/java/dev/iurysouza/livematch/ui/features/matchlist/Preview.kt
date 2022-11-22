package dev.iurysouza.livematch.ui.features.matchlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun NewScreenPreviewSuccess() {
    MatchListScreenComponent(
        state = MatchListState.Success(
            matches = listOf(
                Match(
                    id = "123",
                    homeTeam = Team(
                        crestUrl = "https://crests.football-data.org/764.svg",
                        name = "Brazil",
                        isHomeTeam = true,
                        isAhead = true,
                        score = "3"
                    ),
                    awayTeam = Team(
                        crestUrl = "https://crests.football-data.org/764.svg",
                        name = "Argentina",
                        isHomeTeam = false,
                        isAhead = false,
                        score = "0"
                    ),
                    startTime = "14:00",
                    elapsedMinutes = "20'"
                )
            ),
            isSyncing = true
        )
    )
}

@Preview
@Composable
fun NewScreenPreviewLoading() {
    MatchListScreenComponent(state = MatchListState.Loading)
}