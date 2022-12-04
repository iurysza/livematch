package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MappingError
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMatchCommentsUseCase @Inject constructor(
    private val networkDataSource: RedditNetworkDataSource,
) {

    suspend operator fun invoke(matchId: String): Either<DomainError, List<CommentsEntity>> =
        either {
            networkDataSource.getCommentsForSubmission(
                id = matchId,
                sortBy = "hot"
            )
                .map { it.toCommentsEntity() }
                .mapLeft { MappingError(it.message) }
                .bind()
        }
}

