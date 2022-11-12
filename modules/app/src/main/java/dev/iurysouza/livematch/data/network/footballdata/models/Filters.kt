package dev.iurysouza.livematch.data.network.footballdata.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Filters(
    @Json(name = "dateFrom")
    val dateFrom: String?,
    @Json(name = "dateTo")
    val dateTo: String?,
    @Json(name = "permission")
    val permission: String?
)
