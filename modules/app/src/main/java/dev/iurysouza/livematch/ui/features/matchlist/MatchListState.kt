package dev.iurysouza.livematch.ui.features.matchlist

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

sealed interface MatchListState {
    data class Success(val matchList: List<MatchItem>) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

sealed interface SideEffectError : MatchListEffects
sealed interface MatchListEffects {
    object Idle : MatchListEffects
    data class NavigationError(val msg: ViewError) : SideEffectError
    data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListEffects
}

@JsonClass(generateAdapter = true)
@Parcelize
data class MatchThread(
    val title: String,
    val competition: String,
    val matchDescriptionHtml: String,
    val commentList: List<CommentItem>,
) : Parcelable

@Parcelize
data class CommentItem(
    val author: String,
    val date: String,
    val comment: String,
) : Parcelable

sealed interface ViewError
object InvalidMatchId : ViewError
