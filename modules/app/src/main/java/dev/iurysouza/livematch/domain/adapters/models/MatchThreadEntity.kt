package dev.iurysouza.livematch.domain.adapters.models

data class MatchThreadEntity(
    val id:String,
    val title: String,
    val url: String,
    val score: Int,
    val content: String,
    val contentHtml: String,
    val numComments:Int,
    val createdAt: Long,
)

