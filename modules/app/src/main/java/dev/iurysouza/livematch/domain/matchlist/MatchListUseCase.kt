package dev.iurysouza.livematch.domain.matchlist

import arrow.core.Either
import arrow.core.continuations.either
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchListUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    suspend fun getMatches(): Either<DomainError, List<MatchThreadListEntity>> = either {
        networkDataSource.getMachThreadList().bind()
    }

}
