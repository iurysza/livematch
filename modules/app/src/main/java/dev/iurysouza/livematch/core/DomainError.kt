package dev.iurysouza.livematch.core

sealed interface DomainError

object TokenExpired : DomainError
data class NetworkError(val message: String? = null) : DomainError
object InvalidExpirationDate : DomainError
object TokenNotFound : DomainError
object FailedToSave : DomainError
data class MappingError(val message: String? = null) : DomainError
