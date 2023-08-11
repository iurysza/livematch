package dev.iurysouza.livematch.footballinfo.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Match
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMatchesInfoUseCase @Inject constructor(
  private val networkDataSource: FootballInfoSource,
) {
  suspend fun execute(topLeaguesOnly: Boolean): Either<DomainError, List<Match>> = either {
    networkDataSource
      .fetchLatestMatches(
        LocalDate.now(ZoneOffset.UTC).format(DATE_PATTERN),
      ).map {
        it.response
          .filter { match ->
            if (!topLeaguesOnly) return@filter true
            match.league.id in validLeagues
          }
      }
      .bind()
  }

  private val validLeagues = listOf(
    // International Competitions
    *(1..13).toList().toTypedArray(),
    *(15..22).toList().toTypedArray(),
    *(29..37).toList().toTypedArray(),
    // England
    39,
    41,
    *(45..48).toList().toTypedArray(),
    // Spain
    140, // League
    143, // Cup
    // France
    61, // League
    65, // Cup
    // Italy
    135, // League
    137, // Cup
    // Germany
    78, // League
    81, // Cup
    // Brazil
    71, // League
    73, // Cup
    // Argentina
    128, // League
    130, // Cup
    // Portugal
    94, // League
    96, // Cup
    // Netherlands
    88,
    90,
  )

  companion object {
    private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  }
}
