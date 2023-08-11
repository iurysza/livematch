package dev.iurysouza.livematch.matchday.models

import arrow.core.Either
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.common.navigation.Destination
import dev.iurysouza.livematch.common.navigation.models.MatchThreadArgs
import dev.iurysouza.livematch.footballdata.domain.models.AreaEntity
import dev.iurysouza.livematch.footballdata.domain.models.AwayTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.CompetitionEntity
import dev.iurysouza.livematch.footballdata.domain.models.HalfEntity
import dev.iurysouza.livematch.footballdata.domain.models.HomeTeamEntity
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.footballdata.domain.models.RefereeEntity
import dev.iurysouza.livematch.footballdata.domain.models.ScoreEntity
import dev.iurysouza.livematch.footballdata.domain.models.Status
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Match
import dev.iurysouza.livematch.matchday.R
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import dev.iurysouza.livematch.common.navigation.models.Competition as NavCompetition
import dev.iurysouza.livematch.common.navigation.models.Team as NavTeam

internal fun getValidMatchList(
  matchEntities: List<MatchEntity>,
  savedMatchThreads: List<MatchThreadEntity>,
  resources: ResourceProvider,
  isLiveMode: Boolean,
): ImmutableList<MatchUiModel> = matchEntities.filter {
  if (isLiveMode) {
    it.status == Status.IN_PLAY || it.status == Status.PAUSED
  } else {
    true
  }
}.mapNotNull {
  findValidMatchThread(
    matchId = it.id.toString(),
    matchThreadList = savedMatchThreads,
    matchList = matchEntities,
  )
}.map { (match, matchThread) ->
  val (homeScore, awayScore) = parseMatchScore(matchThread.content)
  match.toUiModel(resources, homeScore, awayScore)
}.toImmutableList()

internal fun createMatchThreadFrom(
  matchId: String,
  matchThreadList: List<MatchThreadEntity>,
  matchList: List<MatchEntity>,
) = Either.catch {
  val matchEntity = matchList.first { it.id.toString() == matchId }
  val matchThreadEntity = matchThreadList.first { matchThread ->
    isMatchRelated(
      homeTeam = matchEntity.homeTeam.name,
      awayTeam = matchEntity.awayTeam.name,
      title = matchThread.title,
    )
  }
  Pair(matchThreadEntity, matchEntity)
}.mapLeft { ViewError.NoMatchFound(it.message.toString()) }
  .map { (matchThreadEntity, matchEntity) ->
    buildMatchThreadWith(
      matchThread = matchThreadEntity,
      matchEntity = matchEntity,
    )
  }

private fun MatchEntity.toUiModel(
  resources: ResourceProvider,
  homeScore: String?,
  awayScore: String?,
) = MatchUiModel(
  id = id.toString(),
  competition = Competition(
    id = competition.id,
    name = competition.name,
    emblemUrl = competition.emblem,
  ),
  homeTeam = toTeam(homeTeam, score, true, homeScore),
  awayTeam = toTeam(awayTeam.asHomeTeam(), score, false, awayScore),
  startTime = formatTime(utcDate),
  elapsedMinutes = status.toText(this, resources),
)

fun formatTime(utcDate: LocalDateTime): String {
  val zoneId = ZoneId.systemDefault()  // get device's default timezone
  val zonedUTCDate = ZonedDateTime.of(utcDate, ZoneId.of("UTC"))
  val zonedLocalDate = zonedUTCDate.withZoneSameInstant(zoneId)  // converts date to local timezone
  return zonedLocalDate.format(
    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()),
  )
}

private fun findValidMatchThread(
  matchId: String,
  matchThreadList: List<MatchThreadEntity>,
  matchList: List<MatchEntity>,
): Pair<MatchEntity, MatchThreadEntity>? {
  val matchEntity = matchList.first { it.id.toString() == matchId }
  val matchThreadEntity = matchThreadList.find { matchThread ->
    isMatchRelated(
      homeTeam = matchEntity.homeTeam.name,
      awayTeam = matchEntity.awayTeam.name,
      title = matchThread.title,
    )
  }
  return if (matchThreadEntity != null) {
    matchEntity to matchThreadEntity
  } else {
    null
  }
}

private val ambiguousWords = listOf(
  "Manchester",
  "Borussia",
  "United",
  "City",
  "Real",
  "Arsenal",
)
private val teamNickNameDictionary = mapOf(
  "Paris Saint-Germain" to "PSG",
  "Manchester United" to "Manchester Utd",
)

/**
 * Checks if the given [title] contains a team name.
 *
 * @param title The title to check for the presence of a team name.
 * @param teamName The team name to search for in the [title].
 * @return `true` if the [title] contains any variations of the [teamName], `false` otherwise.
 */
private fun containsTeamName(title: String, teamName: String): Boolean {
  val wordsSortedByLength = teamName.split(" ").sortedBy { it.length }.reversed()
  val wordsToCheck = buildList {
    add(teamName)
    teamNickNameDictionary[teamName]?.let { add(it) }
    wordsSortedByLength.firstOrNull()?.let { maxLengthWord ->
      if (!ambiguousWords.contains(maxLengthWord)) {
        add(maxLengthWord)
      } else {
        wordsSortedByLength.getOrNull(2)?.let { add(it) }
      }
    }
  }
  return wordsToCheck.any { title.contains(it) }
}

private fun isMatchRelated(
  homeTeam: String,
  awayTeam: String,
  title: String,
): Boolean {
  return containsTeamName(title, homeTeam) || title.contains(awayTeam)
}
fun parseMatchScore(content: String): Pair<String?, String?> = runCatching {
  val pattern = "\\[([0-9]+)(-| – )([0-9]+)\\]".toRegex() // the updated regex pattern
  val matchResults = pattern.find(content)
  val homeScore = matchResults!!.groupValues[1]
  val awayScore = matchResults.groupValues[3]
  homeScore to awayScore
}.getOrElse { null to null }

private fun buildMatchThreadWith(
  matchThread: MatchThreadEntity,
  matchEntity: MatchEntity,
): MatchThread {
  val (homeScore, awayScore) = parseMatchScore(matchThread.content)
  return MatchThread(
    id = matchThread.id,
    title = matchThread.title,
    content = matchThread.content,
    startTime = matchThread.createdAt,
    homeTeam = toTeam(matchEntity.homeTeam, matchEntity.score, true, homeScore),
    awayTeam = toTeam(matchEntity.awayTeam.asHomeTeam(), matchEntity.score, false, awayScore),
    refereeList = matchEntity.referees.map { it.name },
    competition = Competition(
      id = matchEntity.competition.id,
      name = matchEntity.competition.name,
      emblemUrl = matchEntity.competition.emblem,
    ),
  )
}

private fun toTeam(
  homeTeam: HomeTeamEntity,
  score: ScoreEntity,
  isHome: Boolean,
  parsedScore: String? = null,
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
    parsedScore ?: validHomeScore
  } else {
    parsedScore ?: validAwayScore
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

fun MatchThread.toDestination() = Destination.MatchThread(
  matchThread = MatchThreadArgs(
    id = id,
    title = title,
    startTime = startTime,
    content = content,
    homeTeam = NavTeam(
      crestUrl = homeTeam.crestUrl,
      name = homeTeam.name,
      isHomeTeam = homeTeam.isHomeTeam,
      isAhead = homeTeam.isAhead,
      score = homeTeam.score,
    ),
    awayTeam = NavTeam(
      crestUrl = awayTeam.crestUrl,
      name = awayTeam.name,
      isHomeTeam = awayTeam.isHomeTeam,
      isAhead = awayTeam.isAhead,
      score = awayTeam.score,
    ),
    refereeList = refereeList,
    competition = NavCompetition(
      emblemUrl = competition.emblemUrl,
      id = competition.id,
      name = competition.name,
    ),
  ),
)

@Suppress("MagicNumber")
private fun MatchEntity.calculatePlayTime(): String {
  val now = ZonedDateTime.now(ZoneOffset.UTC)
  val duration = Duration.between(utcDate, now)
  val minutes = duration.toMinutes()
  return "${
    if (minutes > 50) {
      minutes - 25
    } else {
      minutes
    }
  }'"
}

private fun Status.toText(matchEntity: MatchEntity, resources: ResourceProvider): String =
  when (this) {
    Status.FINISHED -> resources.getString(R.string.match_status_finished)
    Status.IN_PLAY -> matchEntity.calculatePlayTime()
    Status.PAUSED -> resources.getString(R.string.match_status_paused)
    Status.TIMED -> resources.getString(R.string.match_status_timed)
  }

fun List<Match>.toMatchEntity(): List<MatchEntity> {
  return map { match ->
    val toLocalDateTime: LocalDateTime = OffsetDateTime.parse(match.fixture.date).toLocalDateTime()
    MatchEntity(
      id = match.fixture.id,
      area = AreaEntity(
        name = match.league.country,
        flagUrl = match.league.flag,
      ),
      score = ScoreEntity(
        duration = null,
        fullTime = HalfEntity(
          home = match.score.fulltime.home,
          away = match.score.fulltime.away,
        ),
        halfTime = HalfEntity(
          home = match.score.halftime.home,
          away = match.score.halftime.away,
        ),
        winner = if (match.teams.home.winner == true) match.teams.home.name else match.teams.away.name,
      ),
      status = when (match.fixture.status.short) {
        "FT" -> Status.FINISHED
        "HT" -> Status.PAUSED
        "2H" -> Status.IN_PLAY
        else -> Status.TIMED
      },
      utcDate = toLocalDateTime,
      awayTeam = AwayTeamEntity(
        crest = match.teams.away.logo,
        id = match.teams.away.id,
        name = match.teams.away.name,
      ),
      homeTeam = HomeTeamEntity(
        crest = match.teams.home.logo,
        id = match.teams.home.id,
        name = match.teams.home.name,
      ),
      matchday = null, // This field is not available in `Match`
      lastUpdated = LocalDateTime.now(), // This field is not available in `Match`, use the current time instead
      competition = CompetitionEntity(
        name = match.league.name,
        id = match.league.id,
        emblem = match.league.logo,
      ),
      referees = match.fixture.referee?.let {
        listOf(RefereeEntity(it, ""))
      } ?: emptyList(), // This field is not available in `Match`
    )
  }
}
