package dev.iurysouza.livematch.footballinfo.data

import dev.iurysouza.livematch.footballinfo.BuildConfig
import dev.iurysouza.livematch.footballinfo.domain.newmodel.FootballApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FootballInfoApi {

  @GET("v3/fixtures")
  suspend fun fetchLatestMatches(
    @Header("X-RapidAPI-Key") apiKey: String = BuildConfig.FOOTBALL_INFO_KEY,
    @Header("X-RapidAPI-Host") apiHost: String = BuildConfig.API_FOOTBALL_HOST,
    @Query("date") date: String,
  ): FootballApiResponse
}
