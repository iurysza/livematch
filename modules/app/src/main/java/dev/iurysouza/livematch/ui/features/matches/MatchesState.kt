package dev.iurysouza.livematch.ui.features.matches

import dev.iurysouza.livematch.data.network.footballdata.models.AwayTeam
import dev.iurysouza.livematch.data.network.footballdata.models.HomeTeam
import dev.iurysouza.livematch.data.network.footballdata.models.Score
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

fun toTeam(homeTeam: HomeTeam, score: Score, isHome: Boolean): Team {
    val validHomeScore = if (score.fullTime == null) {
        score.halfTime?.home?.toString() ?: "0"
    } else {
        score.fullTime.home?.toString() ?: "0"
    }

    val validAwayScore = if (score.fullTime == null) {
        score.halfTime?.away?.toString() ?: "0"
    } else {
        score.fullTime.away?.toString() ?: "0"
    }

    val teamScore = if (isHome) {
        validHomeScore
    } else {
        validAwayScore
    }

    val isHomeAhead = validHomeScore.toInt() > validAwayScore.toInt()
    val isAwayAhead = validAwayScore.toInt() > validHomeScore.toInt()

    val isTeamAhead = if (isHome) isHomeAhead else isAwayAhead

    return Team(
        crestUrl = homeTeam.crest,
        name = homeTeam.shortName ?: homeTeam.name!!,
        score = teamScore,
        isAhead = isTeamAhead,
        isHomeTeam = isHome,
    )
}
fun AwayTeam.asHomeTeam() = HomeTeam(
    crest = crest,
    id = id,
    name = name,
    shortName = shortName,
    tla = tla
)
