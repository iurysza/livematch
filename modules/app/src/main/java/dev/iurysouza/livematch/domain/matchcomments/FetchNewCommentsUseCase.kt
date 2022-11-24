package dev.iurysouza.livematch.domain.matchcomments

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.MappingError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.adapters.models.CommentsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchNewCommentsUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    suspend operator fun invoke(matchId: String): Either<DomainError, List<CommentsEntity>> =
        either {
            networkDataSource.getCommentsForSubmission(
                id = matchId,
                sortBy = "new"
            )
                .map { it.toCommentsEntity() }
                .mapLeft { MappingError(it.message) }
                .bind()
        }

}

