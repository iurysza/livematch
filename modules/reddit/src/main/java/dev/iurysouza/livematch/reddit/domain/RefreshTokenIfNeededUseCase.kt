package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.handleErrorWith
import arrow.core.left
import dev.iurysouza.livematch.core.DomainError
import dev.iurysouza.livematch.core.InvalidExpirationDate
import dev.iurysouza.livematch.core.TokenExpired
import dev.iurysouza.livematch.core.TokenNotFound
import dev.iurysouza.livematch.reddit.data.AuthStorage
import dev.iurysouza.livematch.reddit.domain.models.AuthTokenEntity
import dev.iurysouza.livematch.core.isInTheFuture
import dev.iurysouza.livematch.core.nowPlusMillis
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenIfNeededUseCase @Inject constructor(
    private val networkDataSource: RedditNetworkDataSource,
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
