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
    /**
     * This is used to store the html content, since the android navigation library doesn't support
     * deep linking to html content.
     */
    val contentByteArray: ByteArray = ByteArray(0),
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

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + competition.hashCode()
        result = 31 * result + contentByteArray.contentHashCode()
        return result
    }
}

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
    val event: MatchEvent? = null,
    val commentList: List<CommentItem>,
)

@JsonClass(generateAdapter = true)
@Parcelize
data class MediaItem(
    val titleByteArray: ByteArray,
    val urlByteArray: ByteArray,
) : Parcelable {
    val title: String get() = String(titleByteArray)
    val url: String get() = String(urlByteArray)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaItem

        if (!titleByteArray.contentEquals(other.titleByteArray)) return false
        if (!urlByteArray.contentEquals(other.urlByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = titleByteArray.contentHashCode()
        result = 31 * result + urlByteArray.contentHashCode()
        return result
    }
}

@JsonClass(generateAdapter = true)
@Parcelize
data class MatchEvent(
    val relativeTime: String,
    val icon: String,
    val description: String,
) : Parcelable

data class ChampionsLeagueEvent(
    val relativeTime: String?,
    val icon: String?,
    val description: String,
)
