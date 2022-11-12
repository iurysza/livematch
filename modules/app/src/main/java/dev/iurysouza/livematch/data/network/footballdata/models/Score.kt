package dev.iurysouza.livematch.data.network.footballdata.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Score(
    @Json(name = "duration")
    val duration: String?,
    @Json(name = "fullTime")
    val fullTime: FullTime?,
    @Json(name = "halfTime")
    val halfTime: HalfTime?,
    @Json(name = "winner")
    val winner: String?
)
