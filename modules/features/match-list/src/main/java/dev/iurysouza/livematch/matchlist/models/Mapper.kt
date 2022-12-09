package dev.iurysouza.livematch.matchlist.models

import arrow.core.Either
import dev.iurysouza.livematch.footballdata.domain.models.AwayTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.HomeTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.footballdata.domain.models.ScoreEntity
import dev.iurysouza.livematch.footballdata.domain.models.Status
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal fun List<MatchEntity>.toMatchList(): List<MatchUiModel> = map { entity ->
    MatchUiModel(
        id = entity.id.toString(),
        homeTeam = toTeam(entity.homeTeam, entity.score, true),
        awayTeam = toTeam(entity.awayTeam.asHomeTeam(), entity.score, false),
        startTime = entity.utcDate.format(DateTimeFormatter.ofPattern("HH:mm")),
        elapsedMinutes = when (entity.status) {
            Status.FINISHED -> "FT"
            Status.IN_PLAY -> {
                val nowInMilli = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
                val matchStartTimeInMilli = entity.utcDate.toInstant(ZoneOffset.UTC).toEpochMilli()
                // to convert timeDifference from Millis to Minutes:
                // millis -> seconds = divide by 1000
                // seconds -> minutes = divide by 60
                val diffMin = (nowInMilli - matchStartTimeInMilli) / 60000
                "$diffMin'"
            }
            Status.PAUSED -> "HT"
            Status.TIMED -> ""
        }
    )
}

internal fun createMatchThreadFrom(
    selectedMatch: MatchUiModel,
    savedMatchThreadList: List<MatchThreadEntity>,
    savedMatchEntities: List<MatchEntity>,
) = Either.catch {
    val matchThreadEntity = savedMatchThreadList.first { matchThread ->
        val title = matchThread.title
        title.contains(selectedMatch.homeTeam.name) || title.contains(selectedMatch.awayTeam.name)
    }
    val matchEntity = savedMatchEntities.first { it.id.toString() == selectedMatch.id }
    Pair(matchThreadEntity, matchEntity)
}
    .mapLeft { ViewError.NoMatchFound(it.message.toString()) }
    .map { (matchThreadEntity, matchEntity) ->
        buildMatchThreadWith(
            matchThread = matchThreadEntity,
            match = selectedMatch,
            matchEntity = matchEntity
        )
    }

internal fun buildMatchThreadWith(
    matchThread: MatchThreadEntity?,
    match: MatchUiModel,
    matchEntity: MatchEntity,
) = MatchThread(
    id = matchThread?.id,
    content = matchThread?.content,
    startTime = matchThread?.createdAt,
    homeTeam = match.homeTeam,
    awayTeam = match.awayTeam,
    refereeList = matchEntity.referees.map { it.name },
    competition = Competition(
        id = matchEntity.competition.id,
        name = matchEntity.competition.name,
        emblemUrl = matchEntity.competition.emblem,
    )
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

