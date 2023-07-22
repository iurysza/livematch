package dev.iurysouza.livematch.footballinfo.domain.newmodel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FootballApiResponse(
  val errors: List<Any>,
  @Json(name = "get")
  val reqPath: String?,
  val paging: Paging,
  @Json(name = "parameters")
  val reqParam: Parameters?,
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
