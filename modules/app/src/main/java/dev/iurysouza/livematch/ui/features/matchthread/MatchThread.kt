package dev.iurysouza.livematch.ui.features.matchthread

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class MatchThread(
    val id: String = "",
    val title: String,
    val competition: String,
    val startTime: Long,
    val mediaList: List<MediaItem>,
    val content: String,
) : Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class CommentItem(
    val author: String,
    val relativeTime: Int,
    val body: String,
    val score: String,
) : Parcelable

sealed class ViewError(val message: String) {
    data class CommentItemParsingError(val msg: String) : ViewError(msg)
    data class CommentSectionParsingError(val msg: String) : ViewError(msg)
    data class MatchMediaParsingError(val msg: String) : ViewError(msg)
    data class InvalidMatchId(val msg: String) : ViewError(msg)
}


data class CommentSection(
    val name: String,
    val event: MatchEvent,
    val commentList: List<CommentItem>,
)

@JsonClass(generateAdapter = true)
@Parcelize
data class MediaItem(
    val title: String,
    val url: String,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class MatchEvent(
    val relativeTime: String,
    val icon: EventIcon,
    val description: String,
    val keyEvent: Boolean = false,
) : Parcelable
