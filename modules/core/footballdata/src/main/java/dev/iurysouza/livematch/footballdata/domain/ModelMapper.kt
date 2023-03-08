package dev.iurysouza.livematch.footballdata.domain

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
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun MatchListResponse.toMatchEntity(): List<MatchEntity> = matches?.mapNotNull { match ->
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
        emblem = competition.emblem!!,
      ),
      matchday = match.matchday,
      status = Status.valueOf(match.status!!),
      utcDate = LocalDateTime.ofInstant(
        Instant.parse(match.utcDate),
        ZoneOffset.systemDefault(),
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
        Instant.parse(match.lastUpdated),
        ZoneOffset.systemDefault(),
      ),
    )
  }
    .onFailure { Timber.e(it) }
    .getOrNull()
} ?: emptyList()
