package dev.iurysouza.livematch.footballdata.data

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.footballdata.data.models.MatchListResponse
import dev.iurysouza.livematch.footballdata.domain.FootballDataSource
import javax.inject.Inject

class FootballNetworkDataSource @Inject constructor(
  private val api: FootballDataApi,
) : FootballDataSource {
  override suspend fun fetchLatestMatches(
    startDate: String,
    endDate: String,
  ): Either<NetworkError, MatchListResponse> = catch {
    api.fetchLatestMatches(
      dateFrom = "2023-03-06",
      dateTo = "2023-03-07",
    )
  }.mapLeft { NetworkError(it.message) }
}
