package dev.iurysouza.livematch.domain.auth

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.handleErrorWith
import arrow.core.left
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.InvalidExpirationDate
import dev.iurysouza.livematch.domain.TokenExpired
import dev.iurysouza.livematch.domain.TokenNotFound
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val storage: AuthStorage,
) {

    suspend fun refreshTokenIfNeeded(): Either<DomainError, Unit> = either {
        val token = storage.getToken().bind()
        ensure(token.expirationDate.isInTheFuture()) {
            TokenExpired
        }
    }.handleErrorWith { error ->
        when (error) {
            TokenExpired,
            is TokenNotFound,
            -> fetchNewTokenAndSaveIt()
            else -> error.left()
        }
    }

    private fun Long.isInTheFuture() = Instant.ofEpochMilli(this).isAfter(Instant.now())

    private suspend fun fetchNewTokenAndSaveIt(): Either<DomainError, Unit> = either {
        networkDataSource.getAccessToken().bind()
    }.flatMap { (accessToken, expiresIn) ->
        catch {
            AuthToken(
                accessToken,
                expirationDate = Instant.now().plusMillis(expiresIn).toEpochMilli()
            )
        }.mapLeft { InvalidExpirationDate }
    }.map { storage.putToken(it) }
}
