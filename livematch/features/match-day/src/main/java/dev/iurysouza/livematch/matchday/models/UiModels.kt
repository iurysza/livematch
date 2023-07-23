package dev.iurysouza.livematch.matchday.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchUiModel(
  val id: String,
  val homeTeam: Team,
  val awayTeam: Team,
  val startTime: String,
  val elapsedMinutes: String,
  val competition: Competition,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class MatchThread(
  val id: String,
  val title: String,
  val startTime: Long,
  val content: String,
  val homeTeam: Team,
  val awayTeam: Team,
  val refereeList: List<String>,
  val competition: Competition,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Team(
  val crestUrl: String?,
  val name: String,
  val isHomeTeam: Boolean,
  val isAhead: Boolean,
  val score: String,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Competition(
  val emblemUrl: String,
  val id: Int?,
  val name: String,
) : Parcelable

sealed class ViewError(open val message: String) {
  data class NoMatchFound(override val message: String) : ViewError(message)
}
