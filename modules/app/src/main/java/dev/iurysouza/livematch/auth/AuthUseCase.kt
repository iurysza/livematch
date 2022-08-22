package dev.iurysouza.livematch.auth

import android.util.Log
import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.NetworkError

class AuthUseCase(
    private val authRepository: RedditApi,
    private val localStorage: KeyValueStorage,
) {
    companion object {
        private const val ACCESS_TOKEN_KEY = "accessToken"
    }

    suspend fun validateAccessToken(): Either<DomainError, Unit> = either {
        localStorage.get(ACCESS_TOKEN_KEY)
            .mapLeft {
                Log.w("AuthUseCase", "No access token found")
                val token = fetchAccessToken().bind().accessToken
                localStorage.save(ACCESS_TOKEN_KEY, token).bind()
            }
    }

    private suspend fun fetchAccessToken(): Either<DomainError, DomainAccessToken> =
        Either.catch {
            val (accessToken, expiresIn) = authRepository.getAccessToken()
            DomainAccessToken(accessToken, expiresIn)
        }.mapLeft {
            NetworkError
        }
}

data class DomainAccessToken(val accessToken: String, val expiresIn: Int)

