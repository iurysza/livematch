package dev.iurysouza.livematch.footballdata.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.core.DomainError
import dev.iurysouza.livematch.core.MappingError
import dev.iurysouza.livematch.footballdata.data.models.MatchListResponse
import dev.iurysouza.livematch.footballdata.domain.models.AreaEntity
import dev.iurysouza.livematch.footballdata.domain.models.AwayTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.CompetitionEntity
import dev.iurysouza.livematch.footballdata.domain.models.HalfEntity
import dev.iurysouza.livematch.footballdata.domain.models.HomeTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.footballdata.domain.models.RefereeEntity
import dev.iurysouza.livematch.footballdata.domain.models.ScoreEntity
import dev.iurysouza.livematch.footballdata.domain.models.Status
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber


@Singleton
class FetchMatchesUseCase @Inject constructor(
    private val networkDataSource: FootballDataSource,
) {

    suspend operator fun invoke(): Either<DomainError, List<MatchEntity>> =
        either {
            val now = LocalDate.now(ZoneOffset.UTC)
            networkDataSource.fetchLatestMatches(
                startDate = now.format(DATE_PATTERN),
                endDate = now.plusDays(1).format(DATE_PATTERN),
            )
                .map { it.toMatchEntity() }
                .mapLeft { MappingError(it.message) }
                .bind()
        }


    companion object {
        private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}

