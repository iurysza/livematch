package dev.iurysouza.livematch.domain.adapters.models
data class MatchMediaEntity(
    val parentId: String,
    val title: String?,
    val type: String?,
    val html: String?,
    val providerName: String?,
    val providerUrl: String?,
    val authorName: String?,
    val authorUrl: String?,
    val thumbnailUrl: String?,
    val thumbnailWidth: Int?,
    val thumbnailHeight: Int?,
    val width: Int?,
    val height: Int?,
)
