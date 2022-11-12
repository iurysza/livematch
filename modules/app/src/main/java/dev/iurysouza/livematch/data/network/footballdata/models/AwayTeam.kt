package dev.iurysouza.livematch.data.network.footballdata.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AwayTeam(
    @Json(name = "crest")
    val crest: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "shortName")
    val shortName: String?,
    @Json(name = "tla")
    val tla: String?
)
