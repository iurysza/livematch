package dev.iurysouza.livematch.domain.matches

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.data.network.footballdata.FootballDataSource
import dev.iurysouza.livematch.data.network.footballdata.models.MatchListResponse
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.MappingError
import dev.iurysouza.livematch.domain.adapters.models.AreaEntity
import dev.iurysouza.livematch.domain.adapters.models.AwayTeamEntity
import dev.iurysouza.livematch.domain.adapters.models.CompetitionEntity
import dev.iurysouza.livematch.domain.adapters.models.HalfEntity
import dev.iurysouza.livematch.domain.adapters.models.HomeTeamEntity
import dev.iurysouza.livematch.domain.adapters.models.MatchEntity
import dev.iurysouza.livematch.domain.adapters.models.RefereeEntity
import dev.iurysouza.livematch.domain.adapters.models.ScoreEntity
import dev.iurysouza.livematch.domain.adapters.models.Status
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

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

    private fun MatchListResponse.toMatchEntity(): List<MatchEntity> =
        matches?.mapNotNull { match ->
            runCatching {
                val area = match.area!!
                val awayTeam = match.awayTeam!!
                val homeTeam = match.homeTeam!!
                val competition = match.competition!!
                val score = match.score
                MatchEntity(
                    id = match.id!!,
                    area = AreaEntity(
                        area.name,
                        area.flag,
                    ),
                    awayTeam = AwayTeamEntity(
                        id = awayTeam.id!!,
                        crest = awayTeam.crest!!,
                        name = awayTeam.shortName!!,
                    ),
                    homeTeam = HomeTeamEntity(
                        id = homeTeam.id!!,
                        crest = homeTeam.crest!!,
                        name = homeTeam.shortName!!,
                    ),
                    competition = CompetitionEntity(
                        name = competition.name!!,
                        id = competition.id!!,
                        emblem = competition.emblem!!
                    ),
                    matchday = match.matchday!!,
                    status = Status.valueOf(match.status!!),
                    utcDate = LocalDateTime.ofInstant(
                        Instant.parse(match.utcDate), ZoneOffset.systemDefault()
                    ),
                    referees = match.referees!!.map {
                        RefereeEntity(
                            name = it.name!!,
                            type = it.type!!,
                        )
                    },
                    score = ScoreEntity(
                        duration = score!!.duration,
                        halfTime = HalfEntity(
                            away = score.halfTime?.away,
                            home = score.halfTime?.home,
                        ),
                        fullTime = HalfEntity(
                            away = score.fullTime?.away,
                            home = score.fullTime?.home,
                        ),
                        winner = score.winner,
                    ),
                    lastUpdated = LocalDateTime.ofInstant(
                        Instant.parse(match.lastUpdated), ZoneOffset.systemDefault())
                )
            }
                .onFailure { Timber.e(it) }
                .getOrNull()
        } ?: emptyList()
}

