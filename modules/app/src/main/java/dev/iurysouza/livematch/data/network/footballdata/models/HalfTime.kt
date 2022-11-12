package dev.iurysouza.livematch.data.network.footballdata.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HalfTime(
    @Json(name = "away")
    val away: Int?,
    @Json(name = "home")
    val home: Int?
)
