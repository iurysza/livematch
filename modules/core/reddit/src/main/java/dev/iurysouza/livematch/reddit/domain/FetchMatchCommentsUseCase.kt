package dev.iurysouza.livematch.reddit.domain

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.reddit.domain.models.CommentsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMatchCommentsUseCase @Inject constructor(
  private val networkDataSource: RedditNetworkDataSource,
) {

  suspend fun execute(matchId: MatchId): Either<DomainError, List<CommentsEntity>> =
    either {
      networkDataSource.getCommentsForSubmission(
        id = matchId.value,
        sortBy = "hot",
      ).flatMap { it.toCommentsEntity() }.bind()
    }
}
