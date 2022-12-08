package dev.iurysouza.livematch.matchlist.betterarchitecture

import android.os.Parcelable
import dev.iurysouza.livematch.common.storage.ViewEvent
import dev.iurysouza.livematch.common.storage.ViewSideEffect
import dev.iurysouza.livematch.common.storage.ViewState
import dev.iurysouza.livematch.matchlist.Match
import dev.iurysouza.livematch.matchlist.MatchListState
import dev.iurysouza.livematch.matchlist.MatchThread
import kotlinx.parcelize.Parcelize

sealed interface MatchListViewEffect : ViewSideEffect {
    data class Error(val msg: String) : MatchListViewEffect
    data class NavigationError(val msg: String) : MatchListViewEffect
    data class NavigateToMatchThread(val matchThread: MatchThread) : MatchListViewEffect
}

@Parcelize
data class MatchListViewState(
    val matchListState: MatchListState = MatchListState.Loading,
    val isSyncing: Boolean = false,
    val isRefreshing: Boolean = false,
) : ViewState, Parcelable

sealed interface MatchListViewEvent : ViewEvent {
    object Refresh : MatchListViewEvent
    object GetLatestMatches : MatchListViewEvent
    data class NavigateToMatch(val match: Match) : MatchListViewEvent
}
