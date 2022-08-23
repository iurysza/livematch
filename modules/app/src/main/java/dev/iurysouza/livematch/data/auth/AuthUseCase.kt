package dev.iurysouza.livematch.data.auth

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.NetworkError
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
            .mapLeft {
                val authToken = fetchAccessToken().bind()
                storage.putToken(authToken.value, authToken.expirationDate).bind()
            }
            .map { authToken ->
                authToken.expirationDate.isExpired()
            }
    }

    private fun Long.isExpired() =
        Instant.ofEpochMilli(this).isBefore(Instant.now())

    private suspend fun fetchAccessToken(): Either<DomainError, AuthToken> =
        Either.catch {
            val (accessToken, expiresIn) = redditApi.getAccessToken()
            val expirationDate = Instant.now().plusMillis(expiresIn).toEpochMilli()
            AuthToken(accessToken, expirationDate)
        }.mapLeft {
            NetworkError
        }
}
