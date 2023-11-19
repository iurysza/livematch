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
    networkDataSource.fetchLatestMatches(
      date = LocalDate.now(ZoneOffset.UTC).format(DATE_PATTERN),
    ).map {
      it.response.filter { match ->
        if (topLeaguesOnly) {
          match.league.id in topLeagues
        } else {
          true
        }
      }.filter { match ->
        // filter out FA Cup Qualifiers
        if (match.league.id == 45) {
          match.league.round == "Third Round Proper"
        } else {
          true
        }
      }
    }.bind()
  }

  /**
   * | Championship Id | Championship Name | Location |
   * | --- | --- | --- |
   * | 1 | FIFA World Cup Men | International (World) |
   * | 2 | UEFA Champions League | International (Europe) |
   * | 3 | UEFA Europa League | International (Europe) |
   * | 4 | UEFA European Championship | International (Europe) |
   * | 5 | UEFA Nations League | International (Europe) |
   * | 6 | CAF Africa Cup of Nations | International (Africa) |
   * | 960 | Euro Championship - Qualification | International (Europe) |
   * | 8 |  World Cup - Women	 | International (World) |
   * | 9 | CONMEBOL Copa America | International (South America) |
   * | 10 | Friendlies | International (World) |
   * | 667 | Friendlies Clubs | International (World) |
   * | 11 | CONMEBOL Sudamericana | International (South America) |
   * | 13 | CONMEBOL Libertadores | International (South America) |
   * | 15 | FIFA Club World Cup Women | International (World) |
   * | 16 | CONCACAF Champions League | International (North America) |
   * | 17 | AFC Champions League | International (Asia) |
   * | 18 | AFC Cup | International (Asia) |
   * | 19 | African Nations Championship | International (Africa) |
   * | 21 | FIFA Confederations Cup | International (Intercontinental) |
   * | 22 | CONCACAF Gold Cup | International (North America) |
   * | 29 | World Cup - Qualification Africa | International (World) |
   * | 30 | World Cup - Qualification Asia | International (World) |
   * | 31 | World Cup - Qualification CONCACAF | International (World) |
   * | 32 | World Cup - Qualification Europe | International (World) |
   * | 33 | World Cup - Qualification Oceania | International (World) |
   * | 34 | World Cup - Qualification South America | International (World) |
   * | 35 | Asian Cup - Qualification | International (World) |
   * | 36 | Africa Cup of Nations - Qualification | International (Africa) |
   * | 37 | World Cup - Qualification Intercontinental Play-offs | International (World) |
   * | 39 | Premier League | England |
   * | 45 | FA Cup | England |
   * | 48 | EFL Cup | England |
   * | 528 | FA Community Shield | England |
   * | 61 | Ligue 1 | France |
   * | 65 | Coupe de France | France |
   * | 71 | Campeonato Brasileiro Série A | Brazil |
   * | 73 | Copa do Brasil | Brazil |
   * | 78 | Bundesliga | Germany |
   * | 81 | DFB-Pokal | Germany |
   * | 529 | DFL-Supercup | Germany |
   * | 88 | Eredivisie | Netherlands |
   * | 90 | KNVB Cup | Netherlands |
   * | 94 | Primeira Liga | Portugal |
   * | 96 | Taça de Portugal | Portugal |
   * | 547 | Supertaça Cândido de Oliveira | Portugal |
   * | 128 | Argentine Primera División | Argentina |
   * | 130 | Copa Argentina | Argentina |
   * | 135 | Serie A | Italy |
   * | 137 | Coppa Italia | Italy |
   * | 550 | Supercoppa Italiana | Italy |
   * | 140 | LaLiga | Spain |
   * | 143 | Copa del Rey | Spain |
   * | 556 | Supercopa de España | Spain |
   *
   */
  private val topLeagues = mutableSetOf(
    // International Competitions
    *(1..13).toList().toTypedArray(),
    *(15..19).toList().toTypedArray(),
    21,
    22,
    *(29..37).toList().toTypedArray(),
    960,
//    667, friendlies clubs (smaller teams)
    // England
    39,
    45,
    48,
    528,
    // Spain
    140,
    143,
    556,
    // France
    61,
    65,
    // Italy
    135,
    137,
    550,
    // Germany
    78,
    81,
    529,
    // Brazil
    71,
    73,
    // Argentina
    128,
    130,
    // Portugal
    94,
    96,
    547,
    // Netherlands
    88,
    90,
  ).toMutableSet()

  companion object {
    private val DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  }
}
