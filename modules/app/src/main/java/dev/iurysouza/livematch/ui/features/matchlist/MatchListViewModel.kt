package dev.iurysouza.livematch.ui.features.matchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.adapters.models.MatchHighlight
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.domain.auth.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.domain.highlights.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.domain.matchlist.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.ui.features.matchthread.MatchThreadMapper
import dev.iurysouza.livematch.ui.features.matchthread.toMatchItem
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val matchThreadMapper: MatchThreadMapper,
    private val getMatchHighlights: GetMatchHighlightsUseCase,
    private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
    private val fetchLatestMatchThreadsForTodayUseCase: FetchLatestMatchThreadsForTodayUseCase,
) : ViewModel() {

    val events = MutableSharedFlow<MatchListEvents>()

    private val _state = MutableStateFlow<MatchListState>(MatchListState.Loading)
    val state: StateFlow<MatchListState> = _state.asStateFlow()

    private var lastHighlights = emptyList<MatchHighlight>()
    private var lastMatches = emptyList<MatchThreadEntity>()

    private val enabledCompetitions = listOf(
        "English Premier League",
        "Italian Seria A",
        "LaLiga",
        "Ligue 1",
        "German Bundesliga",
    )

    fun getMachList(): Job {
        return viewModelScope.launch {
            either {
                refreshTokenIfNeeded().bind()
                Pair(
                    getMatchHighlights().bind(),
                    fetchLatestMatchThreadsForTodayUseCase().bind()
                )
            }.mapLeft { error ->
                lastMatches = emptyList()
                lastHighlights = emptyList()
                error.toErrorMsg()
            }.map { (highlights, matchList) ->
                lastMatches = matchList
                lastHighlights = highlights
                matchList.toMatchItem(enabledCompetitions)
            }.fold(
                { _state.emit(MatchListState.Error(it)) },
                { _state.emit(MatchListState.Success(it)) }
            )
        }
    }

    fun navigateTo(matchItem: MatchItem) = viewModelScope.launch {
        matchThreadMapper.toMatchThread(
            matchItem = matchItem,
            highlights = lastHighlights,
            lastMatches = lastMatches
        ).fold(
            { events.emit(MatchListEvents.NavigationError(it)) },
            { events.emit(MatchListEvents.NavigateToMatchThread(it)) }
        )
    }

    private fun DomainError.toErrorMsg(): String {
        return when (this) {
            is NetworkError -> {
                Log.e("MatchListViewModel", this.message.toString())
                resourceProvider.getString(R.string.match_screen_error_no_internet)
            }
            else -> resourceProvider.getString(R.string.match_screen_error_default)
        }
    }
}
