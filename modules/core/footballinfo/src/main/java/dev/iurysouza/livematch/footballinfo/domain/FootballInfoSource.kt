package dev.iurysouza.livematch.footballinfo.domain

import arrow.core.Either
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.footballinfo.domain.newmodel.FootballApiResponse

interface FootballInfoSource {
  suspend fun fetchMatchDay(date: String): Either<NetworkError, FootballApiResponse>
  suspend fun fetchLiveMatches(): Either<NetworkError, FootballApiResponse>
}
