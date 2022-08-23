package dev.iurysouza.livematch.data.auth

data class AuthToken(
    val value: String,
    val expirationDate: Long,
)
