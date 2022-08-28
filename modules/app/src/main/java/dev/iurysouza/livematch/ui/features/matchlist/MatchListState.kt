package dev.iurysouza.livematch.ui.features.matchlist

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

sealed interface MatchListState {
    data class Success(val matches: List<MatchItem>) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

@JsonClass(generateAdapter = true)
@Parcelize
data class Post(
    val body: String,
    val id: String,
    val title: String,
    val userId: Int,
    val bgColor: Long,
) : Parcelable

