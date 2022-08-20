package dev.iurysouza.livematch.data.models

import com.squareup.moshi.Json

data class AccessTokenResponse(
    val scope: String,
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_in") val expiresIn: Int,
    @Json(name = "device_id") val deviceId: String,
    @Json(name = "token_type") val tokenType: String
)
