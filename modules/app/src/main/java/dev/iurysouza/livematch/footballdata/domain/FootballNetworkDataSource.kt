package dev.iurysouza.livematch.footballdata.domain

import arrow.core.Either
import arrow.core.Either.Companion.catch
import dev.iurysouza.livematch.core.NetworkError
import dev.iurysouza.livematch.footballdata.data.FootballDataApi
import dev.iurysouza.livematch.footballdata.data.FootballDataSource
import dev.iurysouza.livematch.footballdata.data.models.MatchListResponse
import javax.inject.Inject

class FootballNetworkDataSource @Inject constructor(
    private val api: FootballDataApi,
) : FootballDataSource {
    override suspend fun fetchLatestMatches(
        startDate: String,
        endDate: String,
    ): Either<NetworkError, MatchListResponse> = catch {
        api.fetchLatestMatches(
            dateFrom = startDate,
            dateTo = endDate,
        )
    }.mapLeft { NetworkError(it.message) }

}
