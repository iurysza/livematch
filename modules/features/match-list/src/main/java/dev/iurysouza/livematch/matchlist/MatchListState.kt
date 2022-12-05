package dev.iurysouza.livematch.matchlist

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.domain.models.MediaItem
import kotlinx.parcelize.Parcelize

sealed interface MatchListState {
    data class Success(val matches: List<Match>, val isSyncing: Boolean = false) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

sealed interface MatchListEvents {
    data class Error(val msg: String) : MatchListEvents
    data class NavigationError(val msg: String) : MatchListEvents
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

@Parcelize
@JsonClass(generateAdapter = true)
data class MatchThread(
    val id: String?,
    val startTime: Long?,
    val mediaList: List<MediaItem>,
    val content: String?,
    val homeTeam: Team,
    val awayTeam: Team,
    val refereeList: List<String>,
    val competition: Competition,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Competition(
    val emblemUrl: String,
    val id: Int?,
    val name: String,
) : Parcelable

sealed class ViewError(val message: String) {
    data class InvalidMatchId(val msg: String) : ViewError(msg)
}
