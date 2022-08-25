package dev.iurysouza.livematch.domain.auth

data class AccessTokenEntity(
    val accessToken: String,
    val expiresIn: Long,
    val deviceId: String,
    val tokenType: String,
    val scope: String,
)
