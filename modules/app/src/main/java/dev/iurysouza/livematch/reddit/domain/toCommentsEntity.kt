package dev.iurysouza.livematch.reddit.domain

import dev.iurysouza.livematch.reddit.data.models.entities.Comment
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedCommentData
import dev.iurysouza.livematch.reddit.data.models.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.reddit.data.models.responses.base.Listing
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity

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
