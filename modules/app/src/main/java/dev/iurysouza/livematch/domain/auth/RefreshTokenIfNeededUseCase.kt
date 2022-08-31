package dev.iurysouza.livematch.domain.auth

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.handleErrorWith
import arrow.core.left
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.InvalidExpirationDate
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.adapters.TokenExpired
import dev.iurysouza.livematch.domain.adapters.TokenNotFound
import dev.iurysouza.livematch.domain.adapters.models.AuthTokenEntity
import dev.iurysouza.livematch.util.isInTheFuture
import dev.iurysouza.livematch.util.nowPlusMillis
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenIfNeededUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val storage: AuthStorage,
) {

    suspend operator fun invoke(): Either<DomainError, Unit> = either {
        val token = storage.getToken().bind()
        ensure(token.expirationDate.isInTheFuture()) {
            TokenExpired
        }
    }.handleErrorWith { error ->
        when (error) {
            is TokenNotFound,
            TokenExpired,
            -> fetchNewTokenAndSaveIt()
            else -> error.left()
        }
    }

    private suspend fun fetchNewTokenAndSaveIt(): Either<DomainError, Unit> = either {
        networkDataSource.getAccessToken().bind()
    }.flatMap { (accessToken, expiresIn) ->
        catch {
            AuthTokenEntity(
                value = accessToken,
                expirationDate = nowPlusMillis(expiresIn)
            )
        }.mapLeft { InvalidExpirationDate }
    }.map { storage.putToken(it) }
}
