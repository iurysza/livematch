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
    /**
     * This is used to store the html content, since the android navigation library doesn't support
     * deep linking to html content.
     */
    val contentByteArray: ByteArray = ByteArray(0),
    val comments: List<CommentItem> = emptyList(),
) : Parcelable {
    val content: String get() = String(contentByteArray)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MatchThread

        if (id != other.id) return false
        if (title != other.title) return false
        if (competition != other.competition) return false
        if (!contentByteArray.contentEquals(other.contentByteArray)) return false
        if (comments != other.comments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + competition.hashCode()
        result = 31 * result + contentByteArray.contentHashCode()
        result = 31 * result + comments.hashCode()
        return result
    }
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
