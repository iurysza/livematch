package dev.iurysouza.livematch.domain.auth

data class AuthToken(
    val value: String,
    val expirationDate: Long,
)
