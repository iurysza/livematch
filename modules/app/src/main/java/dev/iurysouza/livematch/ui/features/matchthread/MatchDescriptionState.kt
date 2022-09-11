package dev.iurysouza.livematch.ui.features.matchthread

sealed interface MatchDescriptionState {
    data class Success(val matchThread: MatchThread) : MatchDescriptionState
    object Loading : MatchDescriptionState
    data class Error(val msg: String) : MatchDescriptionState
}

sealed interface MatchCommentsState {
    data class Success(val commentSectionList: List<CommentSection>) : MatchCommentsState
    object Loading : MatchCommentsState
    data class Error(val msg: String) : MatchCommentsState
}
