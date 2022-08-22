package dev.iurysouza.livematch.auth

data class DomainAccessToken(
    val accessToken: String,
    val expiresIn: Int,
)
