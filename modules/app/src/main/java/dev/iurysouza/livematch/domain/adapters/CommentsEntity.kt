package dev.iurysouza.livematch.domain.adapters

data class CommentsEntity(
    val id: String,
    val author: String,
    val fullname: String,
    val body: String,
    val bodyHtml: String,
    val created: Long,
)
