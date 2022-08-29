package dev.iurysouza.livematch.ui.features.matchlist

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

sealed interface MatchListState {
    data class Success(val matchList: List<MatchItem>) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

sealed interface ErrorEvent : MatchListEvents
sealed interface MatchListEvents {
    object Idle : MatchListEvents
    data class NavigationError(val msg: ViewError) : ErrorEvent
    data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListEvents
}

@JsonClass(generateAdapter = true)
@Parcelize
data class MatchThread(
    val title: String,
    val competition: String,
    val contentByteArray: ByteArray = ByteArray(0),
) : Parcelable {
    val content: String get() = String(contentByteArray)
}

@JsonClass(generateAdapter = true)
@Parcelize
data class CommentItem(
    val author: String,
    val date: String,
    val comment: String,
) : Parcelable

sealed interface ViewError
object InvalidMatchId : ViewError
