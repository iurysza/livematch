package dev.iurysouza.livematch.matchthread

sealed interface MatchDescriptionState {
    data class Success(
        val matchThread: MatchThread,
        val matchEvents: List<MatchEvent>,
    ) : MatchDescriptionState

    object Loading : MatchDescriptionState
    data class Error(val msg: String) : MatchDescriptionState
}

sealed interface MatchCommentsState {
    data class Success(val commentSectionList: List<CommentSection>) : MatchCommentsState
    object Loading : MatchCommentsState
    data class Error(val msg: String) : MatchCommentsState
}
