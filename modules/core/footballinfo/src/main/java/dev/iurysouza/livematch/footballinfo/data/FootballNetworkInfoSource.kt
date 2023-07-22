package dev.iurysouza.livematch.footballinfo.data

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.footballinfo.domain.FootballInfoSource
import dev.iurysouza.livematch.footballinfo.domain.newmodel.FootballApiResponse
import javax.inject.Inject

class FootballNetworkInfoSource @Inject constructor(
  private val api: FootballInfoApi,
) : FootballInfoSource {
  override suspend fun fetchLatestMatches(
    date: String,
  ): Either<NetworkError, FootballApiResponse> = catch {
    api.fetchLatestMatches(date = date)
  }.mapLeft { NetworkError(it.message) }
}
