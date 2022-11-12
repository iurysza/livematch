package dev.iurysouza.livematch.data.network.footballdata.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Season(
    @Json(name = "currentMatchday")
    val currentMatchday: Int?,
    @Json(name = "endDate")
    val endDate: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "startDate")
    val startDate: String?,
    @Json(name = "winner")
    val winner: Any?
)
