package dev.iurysouza.livematch.fakes

import arrow.core.Either
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource

class StubNetworkDatasource(
    var returnAccessToken: AccessTokenEntity? = anAccessTokenEntity(),
    private var accessTokenError: Throwable? = null,
) : NetworkDataSource {
    override suspend fun searchFor(
        query: String,
        sort: String,
        timePeriod: String,
        restrictedToSubreddit: Boolean,
        subreddit: String
    ): Either<DomainError, List<MatchThreadEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity> {
        return Either.catch {
            accessTokenError?.let { throw it }
            returnAccessToken!!
        }.mapLeft { NetworkError(it.message) }
    }

    override suspend fun getCommentsForSubmission(id: String): Either<DomainError, List<CommentsEntity>> {
        TODO("Not yet implemented")
    }
}

fun anAccessTokenEntity(
    accessToken: String = "accessToken",
    expiresIn: Long = 8400,
    tokenType: String = "tokenType",
    scope: String = "scope",
    deviceId: String = "deviceId",
): AccessTokenEntity = AccessTokenEntity(
    accessToken = accessToken,
    expiresIn = expiresIn,
    tokenType = tokenType,
    scope = scope,
    deviceId = deviceId
)
