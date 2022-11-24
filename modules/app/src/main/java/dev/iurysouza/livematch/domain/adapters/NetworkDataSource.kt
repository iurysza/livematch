package dev.iurysouza.livematch.domain.adapters

import arrow.core.Either
import dev.iurysouza.livematch.domain.models.AccessTokenResponse
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubmissionListing

interface NetworkDataSource {
    suspend fun searchFor(
        subreddit: String,
        query: String,
        sortBy: String,
        timePeriod: String,
        restrictedToSubreddit: Boolean,
        limit: Int? = null,
    ): Either<NetworkError, EnvelopedSubmissionListing>

    suspend fun getAccessToken(): Either<NetworkError, AccessTokenResponse>
    suspend fun getCommentsForSubmission(id: String, sortBy: String): Either<NetworkError, List<EnvelopedContributionListing>>
}
