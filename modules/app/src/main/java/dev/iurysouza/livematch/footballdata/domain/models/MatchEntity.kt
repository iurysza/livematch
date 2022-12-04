package dev.iurysouza.livematch.footballdata.domain.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class AreaEntity(
    val name: String?,
    val flagUrl: String?,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class MatchEntity(
    val id: Int,
    val area: AreaEntity,
    val score: ScoreEntity,
    val status: Status,
    val utcDate: LocalDateTime,
    val awayTeam: AwayTeamEntity,
    val homeTeam: HomeTeamEntity,
    val matchday: Int,
    val lastUpdated: LocalDateTime,
    val competition: CompetitionEntity,
    val referees: List<RefereeEntity>,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class CompetitionEntity(
    val name: String,
    val id: Int,
    val emblem: String,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class HomeTeamEntity(
    val crest: String,
    val id: Int,
    val name: String,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class AwayTeamEntity(
    val crest: String,
    val id: Int,
    val name: String,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ScoreEntity(
    val duration: String?,
    val fullTime: HalfEntity?,
    val halfTime: HalfEntity?,
    val winner: String?,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class HalfEntity(
    val away: Int?,
    val home: Int?,
) : Parcelable

enum class Status(name: String) {
    IN_PLAY("IN_PLAY"),
    PAUSED("PAUSED"),
    TIMED("TIMED"),
    FINISHED("FINISHED"),
}

@Parcelize
@JsonClass(generateAdapter = true)
data class RefereeEntity(
    val name: String,
    val type: String,
) : Parcelable
