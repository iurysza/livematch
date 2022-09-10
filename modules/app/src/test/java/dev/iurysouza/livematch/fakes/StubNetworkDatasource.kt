package dev.iurysouza.livematch.fakes

import arrow.core.Either
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.models.AccessTokenResponse
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubmissionListing

class StubNetworkDatasource(
    var returnAccessToken: AccessTokenResponse? = anAccessTokenResponse(),
    private var accessTokenError: Throwable? = null,
) : NetworkDataSource {
    override suspend fun getAccessToken(): Either<NetworkError, AccessTokenResponse> {
        return Either.catch {
            accessTokenError?.let { throw it }
            returnAccessToken!!
        }.mapLeft { NetworkError(it.message) }
    }

    override suspend fun searchFor(
        subreddit: String,
        query: String,
        sortBy: String,
        timePeriod: String,
        restrictedToSubreddit: Boolean,
    ): Either<NetworkError, EnvelopedSubmissionListing> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommentsForSubmission(id: String): Either<NetworkError, List<EnvelopedContributionListing>> {
        TODO("Not yet implemented")
    }
}

fun anAccessTokenResponse(
    accessToken: String = "accessToken",
    expiresIn: Long = 8400,
    tokenType: String = "tokenType",
    scope: String = "scope",
    deviceId: String = "deviceId",
): AccessTokenResponse = AccessTokenResponse(
    accessToken = accessToken,
    expiresIn = expiresIn,
    tokenType = tokenType,
    scope = scope,
    deviceId = deviceId
)
