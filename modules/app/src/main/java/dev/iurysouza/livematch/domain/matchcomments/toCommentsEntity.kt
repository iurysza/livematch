package dev.iurysouza.livematch.domain.matchcomments

import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import dev.iurysouza.livematch.domain.models.reddit.entities.Comment
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedCommentData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Listing

@Suppress("UNCHECKED_CAST")
fun List<EnvelopedContributionListing>.toCommentsEntity(): List<CommentsEntity> =
    (last().data as Listing<EnvelopedCommentData>).children
        .map { it.data }
        .toList()
        .filterIsInstance<Comment>()
        .map {
            val flair = it.authorFlairRichtext?.firstOrNull()
            CommentsEntity(
                it.id,
                it.author,
                flair?.url,
                flair?.textRepresentation ?: "",
                it.fullname,
                it.body,
                it.score,
                it.bodyHtml,
                it.createdUtc
            )
        }
