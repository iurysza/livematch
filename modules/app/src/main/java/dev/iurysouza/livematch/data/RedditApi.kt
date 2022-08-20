package dev.iurysouza.livematch.data

import dev.iurysouza.livematch.data.models.AccessTokenResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface RedditApi {

    @POST("api/v1/access_token")
    suspend fun getAccessToken(
        @Query("grant_type") grantType: String = INSTALLED_CLIENT,
        @Query("device_id") deviceId: String = UNTRACKED_DEVICE,
    ): AccessTokenResponse

    companion object {
        private const val UNTRACKED_DEVICE = "DO_NOT_TRACK_THIS_DEVICE"
        private const val INSTALLED_CLIENT = "https://oauth.reddit.com/grants/installed_client"
    }
}
