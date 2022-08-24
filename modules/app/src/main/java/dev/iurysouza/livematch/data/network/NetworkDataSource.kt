package dev.iurysouza.livematch.data.network

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.domain.AccessTokenEntity
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import javax.inject.Inject

class RedditNetworkDataSource @Inject constructor(
    private val redditApi: RedditApi,
) : NetworkDataSource {

    override suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity> =
        catch { redditApi.getAccessToken() }
            .mapLeft { NetworkError(it.message) }
            .map {
                AccessTokenEntity(it.accessToken, it.expiresIn, it.deviceId, it.scope, it.tokenType)
            }

}


