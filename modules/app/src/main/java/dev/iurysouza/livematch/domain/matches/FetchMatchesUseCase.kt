package dev.iurysouza.livematch.domain.matches

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.data.network.footballdata.FootballDataSource
import dev.iurysouza.livematch.data.network.footballdata.models.Area
import dev.iurysouza.livematch.data.network.footballdata.models.AwayTeam
import dev.iurysouza.livematch.data.network.footballdata.models.Competition
import dev.iurysouza.livematch.data.network.footballdata.models.HomeTeam
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import dev.iurysouza.livematch.data.network.footballdata.models.Referee
import dev.iurysouza.livematch.data.network.footballdata.models.Score
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.MappingError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMatchesUseCase @Inject constructor(
    private val networkDataSource: FootballDataSource,
) {

    suspend operator fun invoke(): Either<DomainError, List<MatchEntity>> =
        either {
            networkDataSource.fetchLatestMatches()
                .map { it.toMatchEntity() }
                .mapLeft { MappingError(it.message) }
                .bind()
        }

    private fun MatchListResponse.toMatchEntity(): List<MatchEntity> = matches?.mapNotNull {
        runCatching {
            MatchEntity(
                area = it.area!!,
                id = it.id!!,
                awayTeam = it.awayTeam!!,
                competition = it.competition!!,
                matchday = it.matchday!!,
                homeTeam = it.homeTeam!!,
                status = it.status!!,
                utcDate = it.utcDate!!,
                referees = it.referees!!,
                score = it.score!!
            )
        }.getOrNull()
    } ?: emptyList()
}

data class MatchEntity(
    val area: Area,
    val awayTeam: AwayTeam,
    val competition: Competition,
    val homeTeam: HomeTeam,
    val id: Int,
    val matchday: Int,
    val referees: List<Referee>,
    val score: Score,
    val status: String,
    val utcDate: String,
)
