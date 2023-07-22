package dev.iurysouza.livematch.footballinfo.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.footballinfo.domain.models.Match
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMatchesInfoUseCase @Inject constructor(
  private val networkDataSource: FootballInfoSource,
) {
  suspend fun execute(): Either<DomainError, List<Match>> = either {
    networkDataSource
      .fetchLatestMatches(
        LocalDate.now(ZoneOffset.UTC).format(DATE_PATTERN),
      ).map { it.result }
      .bind()
  }

  companion object {
    private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyyMMdd")
  }
}
