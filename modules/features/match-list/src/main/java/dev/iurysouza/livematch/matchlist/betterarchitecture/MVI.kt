package dev.iurysouza.livematch.matchlist.betterarchitecture

import dev.iurysouza.livematch.common.storage.ViewEvent
import dev.iurysouza.livematch.common.storage.ViewSideEffect
import dev.iurysouza.livematch.common.storage.ViewState
import dev.iurysouza.livematch.matchlist.Match
import dev.iurysouza.livematch.matchlist.MatchListState
import dev.iurysouza.livematch.matchlist.MatchThread

sealed interface MatchListViewEffect : ViewSideEffect {
    data class Error(val msg: String) : MatchListViewEffect
    data class NavigationError(val msg: String) : MatchListViewEffect
    data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListViewEffect
}

data class MatchListViewState(
    val matchListState: MatchListState = MatchListState.Loading,
    val isSyncing: Boolean = false,
) : ViewState

sealed interface MatchListViewEvent : ViewEvent {
    object Refresh : MatchListViewEvent
    object GetLatestMatches : MatchListViewEvent
    data class NavigateToMatch(val match: Match) : MatchListViewEvent

}
