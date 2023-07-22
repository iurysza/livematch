package dev.iurysouza.livematch.footballinfo.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamA(

  val id: String,
  val manager: Manager?,
  val name: String,
  val perf: Perf?,
  val position: String?,
  val score: Score,
//    val stats: Stats?
)
