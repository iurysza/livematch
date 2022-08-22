package dev.iurysouza.livematch.data.auth

data class AuthToken(
    val accessToken: String,
    val expiresIn: Int,
)
