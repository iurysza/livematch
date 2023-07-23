package dev.iurysouza.livematch.footballdata.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.footballdata.data.models.MatchListResponse

interface FootballDataSource {
  suspend fun fetchLatestMatches(
    startDate: String,
    endDate: String,
  ): Either<NetworkError, MatchListResponse>
}
