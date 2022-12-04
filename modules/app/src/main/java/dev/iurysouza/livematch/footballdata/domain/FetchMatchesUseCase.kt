package dev.iurysouza.livematch.footballdata.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MappingError
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


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

