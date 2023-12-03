package dev.iurysouza.livematch.footballinfo.data

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.footballinfo.domain.FootballInfoSource
import dev.iurysouza.livematch.footballinfo.domain.newmodel.FootballApiResponse
import javax.inject.Inject
import timber.log.Timber

class FootballNetworkInfoSource @Inject constructor(
  private val api: FootballInfoApi,
) : FootballInfoSource {
  override suspend fun fetchMatchDay(
    date: String,
  ): Either<NetworkError, FootballApiResponse> = catch { api.fetchLatestMatches(date) }
    .printError()
    .mapLeft { NetworkError(it.message) }

  override suspend fun fetchLiveMatches(): Either<NetworkError, FootballApiResponse> = catch { api.fetchLiveMatches() }
    .printError()
    .mapLeft { NetworkError(it.message) }
}

internal fun <R> Either<Throwable, R>.printError(): Either<Throwable, R> =
  tapLeft { Timber.e(it, "Error fetching matches") }
