package dev.iurysouza.livematch.data.network.footballdata

import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FootballDataApi {
    @GET("v4/matches")
    suspend fun fetchLatestMatches(
        @Header("X-Auth-Token") token: String = BuildConfig.FOOTBALL_KEY,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
    ): MatchListResponse
}
