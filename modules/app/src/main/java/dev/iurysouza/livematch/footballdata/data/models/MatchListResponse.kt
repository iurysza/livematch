package dev.iurysouza.livematch.footballdata.data.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchListResponse(
    @Json(name = "filters")
    val filters: Filters?,
    @Json(name = "matches")
    val matches: List<Match>?,
    @Json(name = "resultSet")
    val resultSet: ResultSet?
)
