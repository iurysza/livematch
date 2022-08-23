package dev.iurysouza.livematch.fakes

import arrow.core.Either
import dev.iurysouza.livematch.domain.AccessTokenEntity
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource

class StubNetworkDatasource(
    var returnAccessToken: AccessTokenEntity? = fakeAccessTokenEntity,
    var returnError: Throwable? = null,
) : NetworkDataSource {
    override suspend fun getAccessToken(): Either<DomainError, AccessTokenEntity> {
        return Either.catch {
            returnError?.let { throw it }
            returnAccessToken!!
        }.mapLeft { NetworkError(it.message ?: "") }
    }
}

val fakeAccessTokenEntity = AccessTokenEntity(
    accessToken = "fakeAccessToken",
    expiresIn = 8400,
    deviceId = "fakeRefreshToken",
    scope = "fakeScope",
    tokenType = "fakeTokenType",
)
