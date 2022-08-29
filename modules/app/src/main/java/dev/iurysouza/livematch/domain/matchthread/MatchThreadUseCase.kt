package dev.iurysouza.livematch.domain.matchthread

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.adapters.CommentsEntity
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchThreadUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    suspend fun getMatchComments(id: String): Either<DomainError, List<CommentsEntity>> = either {
        networkDataSource.getCommentsForSubmission(id).bind()
    }

}
