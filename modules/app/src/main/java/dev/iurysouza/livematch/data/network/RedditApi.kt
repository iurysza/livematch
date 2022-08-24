package dev.iurysouza.livematch.data.network

import dev.iurysouza.livematch.data.models.AccessTokenResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface RedditApi {

    @POST("api/v1/access_token")
    suspend fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Url url: String = "https://www.reddit.com/api/v1/access_token",
        @Query("grant_type") grantType: String = INSTALLED_CLIENT,
        @Query("device_id") deviceId: String = UNTRACKED_DEVICE,
    ): AccessTokenResponse

    companion object {
        private const val UNTRACKED_DEVICE = "DO_NOT_TRACK_THIS_DEVICE"
        private const val INSTALLED_CLIENT = "https://oauth.reddit.com/grants/installed_client"
    }
}
