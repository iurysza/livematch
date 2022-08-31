package dev.iurysouza.livematch.domain.adapters.models

data class AuthTokenEntity(
    val value: String,
    val expirationDate: Long,
)
