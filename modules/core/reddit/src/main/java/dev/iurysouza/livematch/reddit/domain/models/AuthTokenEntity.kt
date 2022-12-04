package dev.iurysouza.livematch.reddit.domain.models

data class AuthTokenEntity(
    val value: String,
    val expirationDate: Long,
)
