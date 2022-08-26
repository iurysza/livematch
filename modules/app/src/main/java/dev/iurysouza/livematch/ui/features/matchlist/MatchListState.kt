package dev.iurysouza.livematch.ui.features.matchlist

import android.os.Parcelable
import dev.iurysouza.livematch.domain.adapters.MatchThreadEntity
import kotlinx.parcelize.Parcelize

sealed interface MatchListState {
    data class Success(val matches: List<MatchThreadEntity>) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

@Parcelize
data class Post(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int,
    val bgColor: Long,
) : Parcelable

