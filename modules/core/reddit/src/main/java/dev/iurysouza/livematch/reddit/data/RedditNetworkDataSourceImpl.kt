package dev.iurysouza.livematch.reddit.data

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.reddit.BuildConfig
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedSubmissionListing
import dev.iurysouza.livematch.reddit.domain.RedditNetworkDataSource
import dev.iurysouza.livematch.reddit.domain.models.AccessTokenResponse
import java.util.Base64
import javax.inject.Inject

class RedditNetworkDataSourceImpl @Inject constructor(
    private val redditApi: RedditApi,
) : RedditNetworkDataSource {

    override suspend fun searchFor(
        subreddit: String,
        query: String,
        sortBy: String,
        timePeriod: String,
        restrictedToSubreddit: Boolean,
        limit: Int?,
    ): Either<NetworkError, EnvelopedSubmissionListing> = catch {
        redditApi.searchFor(
            subreddit = subreddit,
            query = query,
            sort = sortBy,
            timePeriod = timePeriod,
            restrictToSubreddit = restrictedToSubreddit,
            limit = limit
        )
    }.mapLeft { NetworkError(it.message) }

    override suspend fun getAccessToken(): Either<NetworkError, AccessTokenResponse> = catch {
        redditApi.getAccessToken(authorization = buildBasicAuthHeader())
    }.mapLeft { NetworkError(it.message) }

    private fun buildBasicAuthHeader(): String {
        val username = BuildConfig.CLIENT_ID
        val password = ""
        return "Basic ${Base64.getEncoder().encodeToString("$username:$password".toByteArray())}"
    }

    override suspend fun getCommentsForSubmission(
        id: String,
        sortBy: String,
    ): Either<NetworkError, List<EnvelopedContributionListing>> = catch {
        redditApi.fetchComments(id, sorting = sortBy)
    }.mapLeft { NetworkError(it.message) }
}
