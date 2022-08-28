package dev.iurysouza.livematch.data.network

import dev.iurysouza.livematch.data.models.AccessTokenResponse
import dev.iurysouza.livematch.data.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.data.models.reddit.responses.EnvelopedSubmissionListing
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
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

    @GET("r/soccer/search/.json")
    suspend fun getLatestMatchThreadsForToday(
        @Query("q") q: String = "flair:match+thread AND NOT flair:post AND NOT flair:pre",
        @Query("sort") sort: String = "new",
        @Query("t") timePeriod: String = "day",
        @Query("restrict_sr") restrictToSubreddit: Boolean = true,
        @Query("raw_json") rawJson: Int? = 1,
    ): EnvelopedSubmissionListing

    @GET("/comments/{submissionId}/.json")
    suspend fun fetchComments(
        @Path("submissionId") submissionId: String,
        @Query("comment") focusedCommentId: String? = null,
        @Query("context") focusedCommentParentsNum: Int? = null,
        @Query("sort") sorting: String = "top",
        @Query("limit") limit: Long? = null,
        @Query("depth") depth: Int? = 1,
        @Query("raw_json") rawJson: Int? = null,
    ): List<EnvelopedContributionListing>


    companion object {
        private const val UNTRACKED_DEVICE = "DO_NOT_TRACK_THIS_DEVICE"
        private const val INSTALLED_CLIENT = "https://oauth.reddit.com/grants/installed_client"
    }
}
