package dev.iurysouza.livematch.data.auth

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.NetworkError
import dev.iurysouza.livematch.data.repo.TokenExpired
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val redditApi: RedditApi,
    private val storage: AuthStorage,
) {

    suspend fun refreshTokenIfNeeded(): Either<DomainError, Unit> = either {
        storage.getToken()
            .map { authToken ->
                ensure(authToken.expirationDate.isAfterNow()) {
                    TokenExpired
                }
            }
            .mapLeft {
                val authToken = fetchNewAuthToken().bind()
                storage.putToken(authToken).bind()
            }
    }

    private fun Long.isAfterNow() = Instant.ofEpochMilli(this).isAfter(Instant.now())

    private suspend fun fetchNewAuthToken(): Either<DomainError, AuthToken> = catch {
        val (accessToken, expiresIn) = redditApi.getAccessToken()
        val expirationDate = Instant.now().plusMillis(expiresIn).toEpochMilli()
        AuthToken(accessToken, expirationDate)
    }.mapLeft {
        NetworkError
    }
}
