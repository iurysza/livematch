package dev.iurysouza.livematch.data.models.cloned.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.EnvelopedCommentData

@JsonClass(generateAdapter = true)
data class MoreChildrenResponse(

    @Json(name = "json")
    val json: MoreChildrenResponseJSON,

    )

@JsonClass(generateAdapter = true)
data class MoreChildrenResponseJSON(

    @Json(name = "data")
    val data: MoreChildrenResponseThings,

    )

@JsonClass(generateAdapter = true)
data class MoreChildrenResponseThings(

    @Json(name = "things")
    val things: List<EnvelopedCommentData>,

    )