package dev.iurysouza.livematch.data.network.footballdata

import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface FootballDataApi {
    @GET("v4/matches")
    suspend fun fetchLatestMatches(
        @Header("X-Auth-Token") token: String = BuildConfig.FOOTBALL_KEY
    ): MatchListResponse
}
