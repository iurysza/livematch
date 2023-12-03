package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.footballinfo.domain.MatchEvent
import java.time.LocalDateTime


@JsonClass(generateAdapter = true)
data class AreaEntity(
  val name: String?,
  val flagUrl: String?,
)

@JsonClass(generateAdapter = true)
data class MatchEntity(
  val id: Int,
  val area: AreaEntity,
  val score: ScoreEntity,
  val status: Status,
  val utcDate: LocalDateTime,
  val awayTeam: AwayTeamEntity,
  val homeTeam: HomeTeamEntity,
  val matchday: Int?,
  val lastUpdated: LocalDateTime,
  val competition: CompetitionEntity,
  val referees: List<RefereeEntity>,
  val eventList: List<MatchEvent>,
)


@JsonClass(generateAdapter = true)
data class CompetitionEntity(
  val name: String,
  val id: Int,
  val emblem: String,
)


@JsonClass(generateAdapter = true)
data class HomeTeamEntity(
  val crest: String,
  val id: Int,
  val name: String,
)


@JsonClass(generateAdapter = true)
data class AwayTeamEntity(
  val crest: String,
  val id: Int,
  val name: String,
)


@JsonClass(generateAdapter = true)
data class ScoreEntity(
  val duration: String?,
  val fullTime: HalfEntity?,
  val halfTime: HalfEntity?,
  val winner: String?,
)

@JsonClass(generateAdapter = true)

data class HalfEntity(
  val away: Int?,
  val home: Int?,
)

sealed interface Status {
  data class InPlay(val time: String) : Status
  object HalfTime : Status
  object Invalid : Status
  object Finished : Status
}


@JsonClass(generateAdapter = true)
data class RefereeEntity(
  val name: String,
  val type: String,
)
