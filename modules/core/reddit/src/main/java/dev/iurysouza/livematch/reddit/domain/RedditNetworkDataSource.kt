package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedSubmissionListing
import dev.iurysouza.livematch.reddit.domain.models.AccessTokenResponse

interface RedditNetworkDataSource {

  suspend fun searchFor(
    subreddit: String,
    query: String,
    sortBy: String,
    timePeriod: String,
    restrictedToSubreddit: Boolean,
    limit: Int? = null,
    after: String? = null,
  ): Either<NetworkError, EnvelopedSubmissionListing>

  suspend fun getAccessToken(): Either<NetworkError, AccessTokenResponse>

  suspend fun getCommentsForSubmission(
    id: String,
    sortBy: String,
    limit: Int? = null,
    after: String? = null,
  ): Either<NetworkError, List<EnvelopedContributionListing>>
}
