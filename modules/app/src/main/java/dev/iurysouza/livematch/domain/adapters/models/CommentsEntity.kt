package dev.iurysouza.livematch.domain.adapters.models

data class CommentsEntity(
    val id: String,
    val author: String,
    val flairUrl: String?,
    val flairText: String,
    val fullname: String,
    val body: String,
    val score: Int,
    val bodyHtml: String,
    val created: Long,
)
