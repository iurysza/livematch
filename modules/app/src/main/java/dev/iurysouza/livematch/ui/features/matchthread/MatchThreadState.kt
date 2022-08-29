package dev.iurysouza.livematch.ui.features.matchthread

sealed interface MatchThreadState {
    data class Success(val matchThread: MatchThread) : MatchThreadState
    object Loading : MatchThreadState
    data class Error(val msg: String) : MatchThreadState
}
