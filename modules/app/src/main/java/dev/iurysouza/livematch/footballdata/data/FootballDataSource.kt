package dev.iurysouza.livematch.footballdata.data

import arrow.core.Either
import dev.iurysouza.livematch.core.NetworkError
import dev.iurysouza.livematch.footballdata.data.models.MatchListResponse

interface FootballDataSource {
    suspend fun fetchLatestMatches(
        startDate: String,
        endDate: String,
    ): Either<NetworkError, MatchListResponse>
}
