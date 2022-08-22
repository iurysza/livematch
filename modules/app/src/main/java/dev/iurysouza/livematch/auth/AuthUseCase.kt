package dev.iurysouza.livematch.auth

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.NetworkError

class AuthUseCase(
    private val redditApi: RedditApi,
    private val storage: AuthStorage,
) {

    suspend fun validateAccessToken(): Either<DomainError, Unit> = either {
        storage.getToken()
            .mapLeft {
                val token = fetchAccessToken().bind().accessToken
                storage.putToken(token).bind()
            }
    }

    private suspend fun fetchAccessToken(): Either<DomainError, DomainAccessToken> =
        Either.catch {
            val (accessToken, expiresIn) = redditApi.getAccessToken()
            DomainAccessToken(accessToken, expiresIn)
        }.mapLeft {
            NetworkError
        }
}
