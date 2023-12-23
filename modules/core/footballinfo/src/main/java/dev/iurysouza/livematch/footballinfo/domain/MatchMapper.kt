package dev.iurysouza.livematch.footballinfo.domain

import dev.iurysouza.livematch.footballinfo.domain.models.AreaEntity
import dev.iurysouza.livematch.footballinfo.domain.models.AwayTeamEntity
import dev.iurysouza.livematch.footballinfo.domain.models.CompetitionEntity
import dev.iurysouza.livematch.footballinfo.domain.models.HalfEntity
import dev.iurysouza.livematch.footballinfo.domain.models.HomeTeamEntity
import dev.iurysouza.livematch.footballinfo.domain.models.MatchEntity
import dev.iurysouza.livematch.footballinfo.domain.models.RefereeEntity
import dev.iurysouza.livematch.footballinfo.domain.models.ScoreEntity
import dev.iurysouza.livematch.footballinfo.domain.models.Status
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Event
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Match
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Player
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Team
import dev.iurysouza.livematch.footballinfo.domain.newmodel.Time
import java.time.LocalDateTime
import java.time.OffsetDateTime

internal fun List<Match>.toMatchEntity(): List<MatchEntity> =
  filter { it.fixture.status.short != "NS" }
    .map { match ->
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
          "FT" -> Status.Finished
          "HT" -> Status.HalfTime
          "2H", "1H" -> Status.InPlay(match.fixture.status.elapsed.toString())
          else -> Status.Invalid
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
        matchday = null,
        lastUpdated = LocalDateTime.now(),
        competition = CompetitionEntity(
          name = match.league.name,
          id = match.league.id,
          emblem = match.league.logo,
        ),
        referees = match.fixture.referee?.let {
          listOf(RefereeEntity(it, ""))
        } ?: emptyList(),
        eventList = match.events?.toMatchEvent() ?: emptyList(),
      )
    }


sealed class MatchEvent(
  open val time: Time?,
  open val team: Team?,
  open val player: Player?,
  open val assist: Player?,
  open val comments: String?,
) {
  data class Goal(
    val detail: GoalType,
    override val time: Time?,
    override val team: Team?,
    override val player: Player?,
    override val assist: Player?,
    override val comments: String?,
  ) : MatchEvent(
    time = time,
    team = team,
    player = player,
    assist = assist,
    comments = comments,
  )

  data class Card(
    val detail: CardType,
    override val time: Time?,
    override val team: Team?,
    override val player: Player?,
    override val assist: Player?,
    override val comments: String?,
  ) : MatchEvent(
    time = time,
    team = team,
    player = player,
    assist = assist,
    comments = comments,
  )

  data class Subst(
    val playersInAndOut: Pair<Player, Player>,
    override val time: Time?,
    override val team: Team?,
    override val comments: String?,
  ) : MatchEvent(
    time = time,
    team = team,
    player = playersInAndOut.first,
    assist = playersInAndOut.second,
    comments = comments,
  )

  data class Var(
    val detail: VarType,
    override val time: Time?,
    override val team: Team?,
    override val player: Player?,
    override val assist: Player?,
    override val comments: String?,
  ) : MatchEvent(
    time = time,
    team = team,
    player = player,
    assist = assist,
    comments = comments,
  )
}

enum class GoalType {
  NormalGoal, OwnGoal, Penalty, MissedPenalty
}

enum class CardType {
  YellowCard, RedCard
}

enum class VarType {
  GoalCancelled, OffsideGoal, PenaltyConfirmed, Foul
}

fun List<Event>.toMatchEvent(): List<MatchEvent> {
  return this.map { event ->
    when (event.type) {
      "Goal" -> MatchEvent.Goal(
        detail = when (event.detail) {
          "Normal Goal" -> GoalType.NormalGoal
          "Own Goal" -> GoalType.OwnGoal
          "Penalty" -> GoalType.Penalty
          "Missed Penalty" -> GoalType.MissedPenalty
          else -> throw IllegalArgumentException("Unknown goal detail: ${event.detail}")
        },
        time = event.time,
        team = event.team,
        player = event.player,
        assist = event.assist,
        comments = event.comments,
      )

      "Card" -> MatchEvent.Card(
        detail = when (event.detail) {
          "Yellow Card" -> CardType.YellowCard
          "Red Card" -> CardType.RedCard
          else -> throw IllegalArgumentException("Unknown card detail: ${event.detail}")
        },
        time = event.time,
        team = event.team,
        player = event.player,
        assist = event.assist,
        comments = event.comments,
      )

      "subst" -> MatchEvent.Subst(
        playersInAndOut = event.player!! to event.assist!!,
        time = event.time,
        team = event.team,
        comments = event.comments,
      )

      "Var" -> MatchEvent.Var(
        detail = when (event.detail) {
          "Goal Cancelled" -> VarType.GoalCancelled
          "Goal Disallowed - offside" -> VarType.OffsideGoal
          "Goal Disallowed - Foul" -> VarType.Foul
          "Penalty Confirmed" -> VarType.PenaltyConfirmed
          else -> throw IllegalArgumentException("Unknown var detail: ${event.detail}")
        },
        time = event.time,
        team = event.team,
        player = event.player,
        assist = event.assist,
        comments = event.comments,
      )

      else -> throw IllegalArgumentException("Unknown event type: ${event.type}")
    }
  }
}
