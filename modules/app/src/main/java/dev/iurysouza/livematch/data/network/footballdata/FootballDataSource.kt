package dev.iurysouza.livematch.data.network.footballdata

import arrow.core.Either
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import dev.iurysouza.livematch.domain.adapters.NetworkError
import java.time.LocalDate

interface FootballDataSource {
    suspend fun fetchLatestMatches(
        startDate: String,
        endDate: String,
    ): Either<NetworkError, MatchListResponse>
}
