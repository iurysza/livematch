package dev.iurysouza.livematch.fakes

import arrow.core.Either
import dev.iurysouza.livematch.reddit.domain.RedditNetworkDataSource
import dev.iurysouza.livematch.core.NetworkError
import dev.iurysouza.livematch.domain.models.AccessTokenResponse
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubmissionListing

class StubNetworkDatasource(
    var returnAccessToken: AccessTokenResponse? = anAccessTokenResponse(),
    private var accessTokenError: Throwable? = null,
) : RedditNetworkDataSource {
    override suspend fun getAccessToken(): Either<NetworkError, AccessTokenResponse> =
        Either.catch {
            accessTokenError?.let { throw it }
            returnAccessToken!!
        }.mapLeft { NetworkError(it.message) }

    override suspend fun getCommentsForSubmission(
        id: String,
        sortBy: String,
    ): Either<NetworkError, List<EnvelopedContributionListing>> = TODO("Not yet implemented")

    override suspend fun searchFor(
        subreddit: String,
        query: String,
        sortBy: String,
        timePeriod: String,
        restrictedToSubreddit: Boolean,
        limit: Int?,
    ): Either<NetworkError, EnvelopedSubmissionListing> = TODO("Not yet implemented")
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
