package dev.iurysouza.livematch.data.network.footballdata

import arrow.core.Either
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import dev.iurysouza.livematch.domain.adapters.NetworkError

interface FootballDataSource {
    suspend fun fetchLatestMatches(): Either<NetworkError, MatchListResponse>
}
