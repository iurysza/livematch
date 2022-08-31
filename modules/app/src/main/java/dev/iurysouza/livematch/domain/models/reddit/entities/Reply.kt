package dev.iurysouza.livematch.domain.models.reddit.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedComment
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Reply(

    @Json(name = "json")
    val json: ReplyJson,

    ) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class ReplyJson(

    @Json(name = "errors")
    val errors: List<String>,

    @Json(name = "data")
    val data: ReplyData,

    ) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class ReplyData(

    @Json(name = "things")
    val things: List<EnvelopedComment>,

    ) : Parcelable
