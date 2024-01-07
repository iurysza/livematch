package dev.iurysouza.livematch.footballinfo.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.footballinfo.domain.models.MatchEntity
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

  private val topLeaguesOnly = true

  suspend fun execute(): Either<DomainError, List<MatchEntity>> = either {
    val matchDay = networkDataSource.fetchMatchDay(date = LocalDate.now(ZoneOffset.UTC).format(DATE_PATTERN))
      .map { it.response.filterAndMap(topLeaguesOnly) }
      .bind()
    val liveMatches = networkDataSource.fetchLiveMatches()
      .map { it.response.filterAndMap(topLeaguesOnly) }
      .bind()
    (liveMatches + matchDay).distinctBy { it.id }
  }

  private fun List<Match>.filterAndMap(topLeaguesOnly: Boolean): List<MatchEntity> = filter { match ->
    if (topLeaguesOnly) {
      match.league.id in topLeagues
    } else {
      true
    }
  }.filter { match ->
    // filter out FA Cup Qualifiers
    if (match.league.id == 45) {
      match.league.round == "3rd Round"
    } else {
      true
    }
  }.toMatchEntity()


  companion object {
    private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  }
}
