package dev.iurysouza.livematch.domain.matchcomments

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.MappingError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import dev.iurysouza.livematch.domain.models.reddit.entities.Comment
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedCommentData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContributionListing
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Listing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMatchCommentsUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    suspend operator fun invoke(matchId: String): Either<DomainError, List<CommentsEntity>> =
        either {
            networkDataSource.getCommentsForSubmission(matchId)
                .map { it.toCommentsEntity() }
                .mapLeft { MappingError(it.message) }
                .bind()
        }

    @Suppress("UNCHECKED_CAST")
    private fun List<EnvelopedContributionListing>.toCommentsEntity(): List<CommentsEntity> =
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
}

