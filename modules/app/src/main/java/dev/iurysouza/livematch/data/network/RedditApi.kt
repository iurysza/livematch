package dev.iurysouza.livematch.data.network

import dev.iurysouza.livematch.domain.models.AccessTokenResponse
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubmissionListing
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {

    @POST("https://www.reddit.com/api/v1/access_token")
    suspend fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Query("grant_type") grantType: String = INSTALLED_CLIENT,
        @Query("device_id") deviceId: String = UNTRACKED_DEVICE,
    ): AccessTokenResponse

    @GET("r/{subreddit}/search/.json")
    suspend fun searchFor(
        @Path("subreddit") subreddit: String,
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("t") timePeriod: String,
        @Query("restrict_sr") restrictToSubreddit: Boolean,
        @Query("raw_json") rawJson: Int? = 1,
    ): EnvelopedSubmissionListing

    @GET("/comments/{submissionId}/.json")
    suspend fun fetchComments(
        @Path("submissionId") submissionId: String,
        @Query("comment") focusedCommentId: String? = null,
        @Query("context") focusedCommentParentsNum: Int? = null,
        @Query("sort") sorting: String = TOP_RATED,
        @Query("limit") limit: Long? = null,
        @Query("depth") depth: Int? = 1,
        @Query("raw_json") rawJson: Int? = null,
    ): List<EnvelopedContributionListing>

    companion object {
        private const val TOP_RATED = "top"
        private const val UNTRACKED_DEVICE = "DO_NOT_TRACK_THIS_DEVICE"
        private const val INSTALLED_CLIENT = "https://oauth.reddit.com/grants/installed_client"
    }
}
