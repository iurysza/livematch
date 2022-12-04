package dev.iurysouza.livematch.footballdata.data.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Referee(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "nationality")
    val nationality: String?,
    @Json(name = "type")
    val type: String?
)
