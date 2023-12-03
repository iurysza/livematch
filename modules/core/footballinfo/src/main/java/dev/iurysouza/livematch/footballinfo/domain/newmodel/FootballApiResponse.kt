package dev.iurysouza.livematch.footballinfo.domain.newmodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FootballApiResponse(
  @Json(name = "get")
  val reqPath: String?,
  val paging: Paging,
  val response: List<Match>,
  val results: Int,
)

@JsonClass(generateAdapter = true)
data class Paging(
  val current: Int,
  val total: Int,
)

@JsonClass(generateAdapter = true)
data class League(
  val country: String,
  val flag: String?,
  val id: Int,
  val logo: String,
  val name: String,
  val round: String,
  val season: Int,
)

@JsonClass(generateAdapter = true)
data class Match(
  val fixture: Fixture,
  val goals: Goals,
  val league: League,
  val score: Score,
  val teams: Teams,
  val events: List<Event>?,
)

@JsonClass(generateAdapter = true)
data class Fulltime(
  val away: Int?,
  val home: Int?,
)

@JsonClass(generateAdapter = true)
data class Away(
  val id: Int,
  val logo: String,
  val name: String,
  val winner: Boolean?,
)

@JsonClass(generateAdapter = true)
data class Extratime(
  val away: Int?,
  val home: Int?,
)

@JsonClass(generateAdapter = true)
data class Fixture(
  val date: String,
  val id: Int,
  val periods: Periods,
  val referee: String?,
  val status: Status,
  val timestamp: Int,
  val timezone: String,
  val venue: Venue,
)

@JsonClass(generateAdapter = true)
data class Goals(
  val away: Int?,
  val home: Int?,
)

@JsonClass(generateAdapter = true)
data class Halftime(
  val away: Int?,
  val home: Int?,
)

@JsonClass(generateAdapter = true)
data class Venue(
  val city: String?,
  val id: Int?,
  val name: String?,
)

@JsonClass(generateAdapter = true)
data class Teams(
  val away: Away,
  val home: Home,
)

@JsonClass(generateAdapter = true)
data class Home(
  val id: Int,
  val logo: String,
  val name: String,
  val winner: Boolean?,
)

@JsonClass(generateAdapter = true)
data class Status(
  val elapsed: Int?,
  val long: String,
  val short: String,
)

@JsonClass(generateAdapter = true)
data class Score(
  val extratime: Extratime,
  val fulltime: Fulltime,
  val halftime: Halftime,
  val penalty: Penalty,
)

@JsonClass(generateAdapter = true)
data class Periods(
  val first: Int?,
  val second: Int?,
)

@JsonClass(generateAdapter = true)
data class Parameters(
  val date: String,
)

@JsonClass(generateAdapter = true)
data class Penalty(
  val away: Int?,
  val home: Int?,
)

@JsonClass(generateAdapter = true)
data class Event(
  val time: Time?,
  val team: Team?,
  val player: Player?,
  val assist: Player?,
  val type: String?,
  val detail: String?,
  val comments: String?,
)

@JsonClass(generateAdapter = true)
data class Time(
  val elapsed: Int,
  val extra: Int?,
)

@JsonClass(generateAdapter = true)
data class Team(
  val id: Int?,
  val name: String?,
  @Json(name = "logo") val logoUrl: String?,
)

@JsonClass(generateAdapter = true)
data class Player(
  val id: Int?,
  val name: String?,
)
