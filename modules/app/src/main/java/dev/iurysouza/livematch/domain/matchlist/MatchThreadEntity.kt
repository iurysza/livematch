package dev.iurysouza.livematch.domain.matchlist

data class MatchThreadEntity(
    val title: String,
    val url: String,
    val score: Int,
    val content: String,
    val contentHtml: String,
    val numComments:Int,
    val createdAt: Long,
)

