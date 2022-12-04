package dev.iurysouza.livematch.features.matchlist

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.features.matchthread.MatchThread
import dev.iurysouza.livematch.features.matchthread.ViewError
import kotlinx.parcelize.Parcelize

sealed interface MatchListState {
    data class Success(val matches: List<Match>, val isSyncing: Boolean = false) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

sealed interface MatchListEvents {
    data class Error(val msg: String) : MatchListEvents
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
