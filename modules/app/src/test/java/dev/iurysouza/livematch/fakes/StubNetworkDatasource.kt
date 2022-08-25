package dev.iurysouza.livematch.fakes

import arrow.core.Either
import dev.iurysouza.livematch.domain.auth.AccessTokenEntity
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.matchlist.MatchThreadListEntity

class StubNetworkDatasource(
    var returnAccessToken: AccessTokenEntity? = anAccessTokenEntity(),
    private var accessTokenError: Throwable? = null,
) : NetworkDataSource {
    override suspend fun getMachThreadList(): Either<DomainError, List<MatchThreadListEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity> {
        return Either.catch {
            accessTokenError?.let { throw it }
            returnAccessToken!!
        }.mapLeft { NetworkError(it.message) }
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
