package dev.iurysouza.livematch.data.auth

import arrow.core.Either
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.FailedToSave
import dev.iurysouza.livematch.data.repo.KeyNotFound
import dev.iurysouza.livematch.data.storage.KeyValueStorage
import javax.inject.Inject
import javax.inject.Singleton

class AuthStorage @Inject constructor(
    private val storage: KeyValueStorage,
) {

    fun getToken(): Either<DomainError, AuthToken> =
        Either.catch {
            AuthToken(
                value = storage.getString(ACCESS_TOKEN_KEY)!!,
                expirationDate = storage.getLong(EXPIRATION_DATE_KEY)!!
            )
        }.mapLeft {
            KeyNotFound
        }

    fun putToken(accessToken: String, expiresIn: Long): Either<DomainError, Unit> =
        Either.catch {
            storage.put(EXPIRATION_DATE_KEY, expiresIn)
            storage.put(ACCESS_TOKEN_KEY, accessToken)
        }.mapLeft {
            FailedToSave
        }
}

private const val EXPIRATION_DATE_KEY = "expirationDate"
private const val ACCESS_TOKEN_KEY = "accessToken"
