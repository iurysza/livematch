package dev.iurysouza.livematch.domain

sealed interface DomainError

object TokenExpired : DomainError
data class NetworkError(val message: String? = null) : DomainError
object InvalidExpirationDate : DomainError
object TokenNotFound : DomainError
object FailedToSave : DomainError
data class SerializationError(val message: String? = null) : DomainError
