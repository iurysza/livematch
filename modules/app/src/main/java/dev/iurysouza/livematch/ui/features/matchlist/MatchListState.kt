package dev.iurysouza.livematch.ui.features.matchlist

import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.features.matchthread.ViewError

sealed interface MatchListState {
    data class Success(val matchList: List<MatchItem>) : MatchListState
    object Loading : MatchListState
    data class Error(val msg: String) : MatchListState
}

sealed interface MatchListEvents {
    object Idle : MatchListEvents
    data class NavigationError(val msg: ViewError) : MatchListEvents
    data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListEvents
}

