package dev.iurysouza.livematch.reddit.data

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.FailedToSave
import dev.iurysouza.livematch.common.storage.KeyValueStorage
import dev.iurysouza.livematch.common.TokenNotFound
import dev.iurysouza.livematch.reddit.domain.models.AuthTokenEntity
import javax.inject.Inject

class AuthStorage @Inject constructor(
    private val storage: KeyValueStorage,
) {

    fun getToken(): Either<DomainError, AuthTokenEntity> = catch {
        AuthTokenEntity(
            value = storage.getString(ACCESS_TOKEN_KEY)!!,
            expirationDate = storage.getLong(EXPIRATION_DATE_KEY)!!
        )
    }.mapLeft {
        TokenNotFound
    }

    fun putToken(token: AuthTokenEntity): Either<DomainError, Unit> = catch {
        storage.put(EXPIRATION_DATE_KEY, token.expirationDate)
        storage.put(ACCESS_TOKEN_KEY, token.value)
    }.mapLeft {
        FailedToSave
    }
}

private const val EXPIRATION_DATE_KEY = "expirationDate"
private const val ACCESS_TOKEN_KEY = "accessToken"
