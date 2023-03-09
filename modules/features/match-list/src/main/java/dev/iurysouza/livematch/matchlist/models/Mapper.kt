package dev.iurysouza.livematch.matchlist.models

import arrow.core.Either
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.footballdata.domain.models.AwayTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.HomeTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.footballdata.domain.models.ScoreEntity
import dev.iurysouza.livematch.footballdata.domain.models.Status
import dev.iurysouza.livematch.matchlist.R
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal fun MatchEntity.toUiModel(resources: ResourceProvider) = MatchUiModel(
  id = id.toString(),
  homeTeam = toTeam(homeTeam, score, true),
  awayTeam = toTeam(awayTeam.asHomeTeam(), score, false),
  startTime = utcDate.format(DateTimeFormatter.ofPattern("HH:mm")),
  elapsedMinutes = status.toText(this, resources),
)

private fun MatchEntity.calculatePlayTime(): String {
  val nowInMilli = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
  val matchStartTimeInMilli = utcDate.toInstant(ZoneOffset.UTC).toEpochMilli()
  // to convert timeDifference from Millis to Minutes:
  // millis -> seconds = divide by 1000
  // seconds -> minutes = divide by 60
  val diffMin = (nowInMilli - matchStartTimeInMilli) / 60000
  return "$diffMin'"
}

private fun Status.toText(matchEntity: MatchEntity, resources: ResourceProvider): String =
  when (this) {
    Status.FINISHED -> resources.getString(R.string.match_status_finished)
    Status.IN_PLAY -> matchEntity.calculatePlayTime()
    Status.PAUSED -> resources.getString(R.string.match_status_paused)
    Status.TIMED -> resources.getString(R.string.match_status_timed)
  }

internal fun List<MatchEntity>.toMatchList(
  resources: ResourceProvider,
): List<MatchUiModel> = map { it.toUiModel(resources) }

internal fun createMatchThreadFrom(
  matchId: String,
  matchThreadList: List<MatchThreadEntity>,
  matchList: List<MatchEntity>,
) = Either.catch {
  val matchEntity = matchList.first { it.id.toString() == matchId }
  val matchThreadEntity = matchThreadList.first { matchThread ->
    val title = matchThread.title
    title.contains(matchEntity.homeTeam.name) || title.contains(matchEntity.awayTeam.name)
  }
  Pair(matchThreadEntity, matchEntity)
}.mapLeft { ViewError.NoMatchFound(it.message.toString()) }
  .map { (matchThreadEntity, matchEntity) ->
    buildMatchThreadWith(
      matchThread = matchThreadEntity,
      matchEntity = matchEntity,
    )
  }

internal fun buildMatchThreadWith(
  matchThread: MatchThreadEntity?,
  matchEntity: MatchEntity,
): MatchThread = MatchThread(
  id = matchThread?.id,
  content = matchThread?.content,
  startTime = matchThread?.createdAt,
  homeTeam = toTeam(matchEntity.homeTeam, matchEntity.score, true),
  awayTeam = toTeam(matchEntity.awayTeam.asHomeTeam(), matchEntity.score, false),
  refereeList = matchEntity.referees.map { it.name },
  competition = Competition(
    id = matchEntity.competition.id,
    name = matchEntity.competition.name,
    emblemUrl = matchEntity.competition.emblem,
  ),
  mediaList = emptyList(),
)

internal fun toTeam(
  homeTeam: HomeTeamEntity,
  score: ScoreEntity,
  isHome: Boolean,
): Team {
  val validHomeScore = if (score.fullTime == null) {
    score.halfTime?.home?.toString() ?: "0"
  } else {
    score.fullTime?.home?.toString() ?: "0"
  }

  val validAwayScore = if (score.fullTime == null) {
    score.halfTime?.away?.toString() ?: "0"
  } else {
    score.fullTime?.away?.toString() ?: "0"
  }

  val teamScore = if (isHome) {
    validHomeScore
  } else {
    validAwayScore
  }

  val isHomeAhead = validHomeScore.toInt() > validAwayScore.toInt()
  val isAwayAhead = validAwayScore.toInt() > validHomeScore.toInt()

  val isTeamAhead = if (isHome) isHomeAhead else isAwayAhead

  return Team(
    crestUrl = homeTeam.crest,
    name = homeTeam.name,
    score = teamScore,
    isAhead = isTeamAhead,
    isHomeTeam = isHome,
  )
}

private fun AwayTeamEntity.asHomeTeam() = HomeTeamEntity(
  crest = crest,
  id = id,
  name = name,
)
