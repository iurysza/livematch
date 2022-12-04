package dev.iurysouza.livematch.reddit.data.models.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Karma(

    @Json(name = "sr")
    val subreddit: String,

    @Json(name = "comment_karma")
    val commentKarma: Int,

    @Json(name = "link_karma")
    val linkKarma: Int,

    ) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class KarmaList(

    @Json(name = "kind")
    val kind: EnvelopeKind,

    @Json(name = "data")
    val data: List<Karma>,

    ) : Parcelable
