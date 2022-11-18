package dev.iurysouza.livematch.ui.features.matchlist

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.network.footballdata.models.AwayTeam
import dev.iurysouza.livematch.data.network.footballdata.models.HomeTeam
import dev.iurysouza.livematch.data.network.footballdata.models.Score
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.features.matchthread.ViewError
import kotlinx.parcelize.Parcelize

sealed interface MatchesState {
    data class Success(val matches: List<Match>) : MatchesState
    object Loading : MatchesState
    data class Error(val msg: String) : MatchesState
}

sealed interface MatchListEvents {
    data class NavigationError(val msg: ViewError) : MatchListEvents
    data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListEvents
}

data class Match(
    val id: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val startTime: String,
    val elapsedMinutes: String,
)

@Parcelize
@JsonClass(generateAdapter = true)
data class Team(
    val crestUrl: String?,
    val name: String,
    val isHomeTeam: Boolean,
    val isAhead: Boolean,
    val score: String,
) : Parcelable

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

data class MatchItem(
    val id: String = "",
    val title: String,
    val competition: String,
)
