package dev.iurysouza.livematch.common

sealed class DomainError(open val throwable: Throwable? = null)

object TokenExpired : DomainError()
data class NetworkError(val message: String? = null) : DomainError()
object InvalidExpirationDate : DomainError()
object TokenNotFound : DomainError()
object FailedToSave : DomainError()
data class MappingError(val message: String? = null, override val throwable: Throwable? = null) : DomainError(throwable)
