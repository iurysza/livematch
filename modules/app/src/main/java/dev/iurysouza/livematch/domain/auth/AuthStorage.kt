package dev.iurysouza.livematch.domain.auth

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.FailedToSave
import dev.iurysouza.livematch.domain.TokenNotFound
import dev.iurysouza.livematch.domain.adapters.KeyValueStorage
import javax.inject.Inject

class AuthStorage @Inject constructor(
    private val storage: KeyValueStorage,
) {

    fun getToken(): Either<DomainError, AuthToken> = catch {
        AuthToken(
            value = storage.getString(ACCESS_TOKEN_KEY)!!,
            expirationDate = storage.getLong(EXPIRATION_DATE_KEY)!!
        )
    }.mapLeft {
        TokenNotFound
    }

    fun putToken(token: AuthToken): Either<DomainError, Unit> = catch {
        storage.put(EXPIRATION_DATE_KEY, token.expirationDate)
        storage.put(ACCESS_TOKEN_KEY, token.value)
    }.mapLeft {
        FailedToSave
    }
}

private const val EXPIRATION_DATE_KEY = "expirationDate"
private const val ACCESS_TOKEN_KEY = "accessToken"
