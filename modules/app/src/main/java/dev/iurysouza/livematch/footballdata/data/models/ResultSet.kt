package dev.iurysouza.livematch.footballdata.data.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultSet(
    @Json(name = "competitions")
    val competitions: String?,
    @Json(name = "count")
    val count: Int?,
    @Json(name = "first")
    val first: String?,
    @Json(name = "last")
    val last: String?,
    @Json(name = "played")
    val played: Int?
)
