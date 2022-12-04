package dev.iurysouza.livematch.reddit.domain

import dev.iurysouza.livematch.reddit.data.models.entities.Comment
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedCommentData
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedSubmissionListing
import dev.iurysouza.livematch.reddit.data.models.responses.base.Listing
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity

@Suppress("UNCHECKED_CAST")
fun List<EnvelopedContributionListing>.toCommentsEntity(): List<CommentsEntity> =
    (last().data as Listing<EnvelopedCommentData>).children
        .map { it.data }
        .toList()
        .filterIsInstance<Comment>()
        .map {
            val flair = it.authorFlairRichtext?.firstOrNull()
            CommentsEntity(
                id = it.id,
                author = it.author,
                flairUrl = flair?.url,
                flairText = flair?.textRepresentation ?: "",
                fullname = it.fullname,
                body = it.body,
                score = it.score,
                bodyHtml = it.bodyHtml,
                created = it.createdUtc
            )
        }

fun EnvelopedSubmissionListing.matchHighlightEntities() = data.children.map { child ->
    runCatching {
        val data = child.data
        val media = data.media!!.oEmbed!!
        MatchHighlightEntity(
            parentId = data.id,
            title = data.title,
            type = media.type ?: "video",
            html = media.html,
            providerName = media.providerName,
            providerUrl = media.providerUrl,
            authorName = data.author,
            authorUrl = media.authorUrl,
            thumbnailUrl = media.thumbnailUrl,
            thumbnailWidth = media.thumbnailWidth,
            thumbnailHeight = media.thumbnailHeight,
            width = media.width,
            height = media.height,
            createdAt = data.createdUtc,
        )
    }.getOrNull() ?: MatchHighlightEntity(
        parentId = child.data.id,
        title = child.data.title,
        type = "video",
        html = child.data.url,
        providerName = "Reddit",
        providerUrl = "other",
        authorName = child.data.author,
        authorUrl = "https://reddit.com/u/${child.data.author}",
        thumbnailUrl = null,
        thumbnailWidth = null,
        thumbnailHeight = null,
        width = null,
        height = null,
        createdAt = child.data.createdUtc,
    )
}

fun EnvelopedSubmissionListing.matchThreadEntities() = data.children.map { child ->
    MatchThreadEntity(
        id = child.data.id,
        title = child.data.title,
        url = child.data.url,
        score = child.data.score,
        numComments = child.data.numComments,
        createdAt = child.data.createdUtc,
        content = child.data.selfText ?: "",
        contentHtml = child.data.selfTextHtml ?: "",
    )
}
