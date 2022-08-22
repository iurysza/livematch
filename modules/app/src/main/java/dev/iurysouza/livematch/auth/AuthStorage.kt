package dev.iurysouza.livematch.auth

import arrow.core.Either
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.FailedToSave
import dev.iurysouza.livematch.data.repo.KeyNotFound
import dev.iurysouza.livematch.data.storage.KeyValueStorage

class AuthStorage(
    private val storage: KeyValueStorage,
) {

    fun getToken(): Either<DomainError, String> =
        Either.catch {
            storage.getString(ACCESS_TOKEN_KEY)!!
        }.mapLeft {
            KeyNotFound
        }

    fun putToken(value: String): Either<DomainError, Unit> =
        Either.catch {
            storage.putString(ACCESS_TOKEN_KEY, value)
        }.mapLeft {
            FailedToSave
        }
}

private const val ACCESS_TOKEN_KEY = "accessToken"
