package dev.iurysouza.livematch.footballinfo.data

import dev.iurysouza.livematch.footballinfo.BuildConfig
import dev.iurysouza.livematch.footballinfo.domain.models.FootballInfoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FootballInfoApi {

  @GET("matches/day/basic/")
  suspend fun fetchLatestMatches(
    @Header("X-RapidAPI-Key") apiKey: String = BuildConfig.FOOTBALL_KEY,
    @Header("X-RapidAPI-Host") apiHost: String = BuildConfig.FOOTBALL_INFO_HOST,
    @Query("d") date: String,
  ): FootballInfoResponse
}
