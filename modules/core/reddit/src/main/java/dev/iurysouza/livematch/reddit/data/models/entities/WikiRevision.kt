package dev.iurysouza.livematch.reddit.data.models.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedRedditor
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class WikiRevision(

    @Json(name = "id")
    val id: String,

    @Json(name = "replies")
    val authorRaw: EnvelopedRedditor?,

    @Transient
    val author: Redditor? = authorRaw?.data,

    @Json(name = "revision_hidden")
    val revisionHidden: Boolean,

    @Json(name = "reason")
    val reason: String?,

    @Json(name = "page")
    val page: String,

    @Json(name = "timestamp")
    val timestamp: Long,

    ) : Parcelable
