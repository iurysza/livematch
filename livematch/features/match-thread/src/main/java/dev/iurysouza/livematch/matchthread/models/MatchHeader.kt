package dev.iurysouza.livematch.matchthread.models

import dev.iurysouza.livematch.common.navigation.models.MatchThreadArgs

data class MatchHeader(
  val homeTeam: HeaderTeam,
  val awayTeam: HeaderTeam,
  val competition: String,
  val competitionLogo: String,
)

data class HeaderTeam(
  val name: String,
  val crestUrl: String,
  val score: String,
)

fun MatchThreadArgs.toMatchHeader(): MatchHeader = MatchHeader(
  homeTeam = HeaderTeam(
    name = homeTeam.name,
    crestUrl = homeTeam.crestUrl ?: "",
    score = homeTeam.score,
  ),
  awayTeam = HeaderTeam(
    name = awayTeam.name,
    crestUrl = awayTeam.crestUrl ?: "",
    score = awayTeam.score,
  ),
  competition = competition.name,
  competitionLogo = competition.emblemUrl,
)
