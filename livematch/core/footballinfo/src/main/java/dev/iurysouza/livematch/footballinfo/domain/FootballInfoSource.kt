package dev.iurysouza.livematch.footballinfo.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.footballinfo.domain.newmodel.FootballApiResponse

interface FootballInfoSource {
  suspend fun fetchLatestMatches(
    date: String,
  ): Either<NetworkError, FootballApiResponse>
}
