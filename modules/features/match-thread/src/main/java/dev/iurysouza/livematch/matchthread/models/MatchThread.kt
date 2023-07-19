package dev.iurysouza.livematch.matchthread.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.common.navigation.models.MatchThreadArgs
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MatchThread(
  val id: String,
  val startTime: Long,
  val mediaList: List<MediaItem>,
  val content: String,
  val homeTeam: Team,
  val awayTeam: Team,
  val refereeList: List<String>,
  val competition: Competition,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class MediaItem(
  val title: String,
  val url: String,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Competition(
  val emblemUrl: String,
  val id: Int?,
  val name: String,
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
data class MatchEvent(
  val relativeTime: String,
  val icon: EventIcon,
  val description: String,
  val keyEvent: Boolean = false,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class CommentItem(
  val author: String,
  val relativeTime: Int,
  val body: String,
  val score: String,
  val flairUrl: String?,
  val flairName: String,
) : Parcelable

sealed class ViewError(val message: String) {
  data class CommentItemParsingError(val msg: String) : ViewError(msg)
  data class CommentSectionParsingError(val msg: String) : ViewError(msg)
}

@Parcelize
data class CommentSection(
  val name: String,
  val event: MatchEvent,
  val commentList: List<CommentItem>,
) : Parcelable

fun MatchThreadArgs.toUi() = MatchThread(
  id = id!!,
  startTime = startTime!!,
  mediaList = emptyList(),
  content = content!!,
  homeTeam = Team(
    crestUrl = homeTeam.crestUrl,
    name = homeTeam.name,
    isHomeTeam = homeTeam.isHomeTeam,
    isAhead = homeTeam.isAhead,
    score = homeTeam.score,
  ),
  awayTeam = Team(
    crestUrl = awayTeam.crestUrl,
    name = awayTeam.name,
    isHomeTeam = awayTeam.isHomeTeam,
    isAhead = awayTeam.isAhead,
    score = awayTeam.score,
  ),
  refereeList = refereeList,
  competition = Competition(
    emblemUrl = competition.emblemUrl,
    id = competition.id,
    name = competition.name,
  ),
)
