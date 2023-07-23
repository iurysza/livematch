package dev.iurysouza.livematch.reddit.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(
  @Json(name = "access_token") val accessToken: String,
  @Json(name = "expires_in") val expiresIn: Long,
  @Json(name = "device_id") val deviceId: String,
  @Json(name = "token_type") val tokenType: String,
  val scope: String,
)
