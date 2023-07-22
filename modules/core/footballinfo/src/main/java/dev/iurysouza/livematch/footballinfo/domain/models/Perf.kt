package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Perf(
  val avgGameGoals: String,
  val avgGoalsConceded: String,
  val avgGoalsScored: String,
  val btts: String,
  val goalsConceded015: List<String>,
  val goalsConceded1630: List<String>,
  val goalsConceded3145: List<String>,
  val goalsConceded4660: List<String>,
  val goalsConceded6175: List<String>,
  val goalsConceded7690: List<String>,
  val goalsScored015: List<String>,
  val goalsScored1630: List<String>,
  val goalsScored3145: List<String>,
  val goalsScored4660: List<String>,
  val goalsScored6175: List<String>,
  val goalsScored7690: List<String>,
  val l5Matches: String,
  val o05Game: String,
  val o15Game: String,
  val o15Team: String,
  val o25Game: String,
  val o35Game: String,
  val totGoalsConceded: String,
  val totGoalsScored: String,
)
