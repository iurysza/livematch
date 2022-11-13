package dev.iurysouza.livematch.ui.features.matches

import dev.iurysouza.livematch.domain.matches.MatchEntity
import dev.iurysouza.livematch.ui.features.matchthread.ViewError

sealed interface MatchesState {
    data class Success(val matches: List<Match>) : MatchesState
    object Loading : MatchesState
    data class Error(val msg: String) : MatchesState
}

sealed interface MatchesEvents {
    object Idle : MatchesEvents
    data class NavigationError(val msg: ViewError) : MatchesEvents
    data class NavigateToMatchThread(val matchThread: MatchEntity) : MatchesEvents
}

data class Match(
    val id: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val startTime: String,
    val elapsedMinutes: String,
)

data class Team(
    val crestUrl: String?,
    val name: String,
    val isHomeTeam: Boolean,
    val isAhead: Boolean,
    val score: String,
)
