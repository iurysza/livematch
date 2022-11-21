package dev.iurysouza.livematch.ui.features.matchlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.adapters.models.MatchEntity
import dev.iurysouza.livematch.domain.adapters.models.MatchHighlight
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.domain.auth.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.domain.highlights.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.domain.matches.FetchMatchesUseCase
import dev.iurysouza.livematch.domain.matchthreads.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.ui.features.matchthread.MatchHighlightParser
import dev.iurysouza.livematch.ui.features.matchthread.ViewError
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

private const val KEY_MATCH_THREAD = "matchThreads"
private const val KEY_HIGHLIGHTS = "highlights"
private const val KEY_MATCHES = "matches"

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider,
    private val fetchMatches: FetchMatchesUseCase,
    private val highlightParser: MatchHighlightParser,
    private val getMatchHighlights: GetMatchHighlightsUseCase,
    private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
    private val fetchLatestMatchThreadsForTodayUseCase: FetchLatestMatchThreadsForTodayUseCase,
) : ViewModel() {

    val events = MutableSharedFlow<MatchListEvents>()

    private val matchHighlightsFlow = savedStateHandle.getStateFlow(
        key = KEY_HIGHLIGHTS,
        initialValue = emptyList<MatchHighlight>()
    )
    private val matchThreadListFlow = savedStateHandle.getStateFlow(
        key = KEY_MATCH_THREAD,
        initialValue = emptyList<MatchThreadEntity>()
    )
    private val matchEntityListFlow = savedStateHandle.getStateFlow(
        key = KEY_MATCHES,
        initialValue = emptyList<MatchEntity>()
    )

    private val _state = MutableStateFlow<MatchListState>(MatchListState.Loading)
    val state: StateFlow<MatchListState> = _state.asStateFlow()

    init {
        fetchMathThreadsAndHighlights()
    }

    fun getLatestMatches() = viewModelScope.launch {
        val savedMatches = savedStateHandle.get<List<MatchEntity>>(KEY_MATCHES)
        if (!savedMatches.isNullOrEmpty()) {
            _state.emit(MatchListState.Success(savedMatches.toMatchList()))
        } else {
            savedStateHandle[KEY_MATCHES] = emptyList<MatchEntity>()
            either { fetchMatches().bind() }
                .map { matchEntityList ->
                    savedStateHandle[KEY_MATCHES] = matchEntityList
                    matchEntityList.toMatchList()
                }
                .mapLeft { _state.emit(MatchListState.Error(it.toString())) }
                .map { _state.emit(MatchListState.Success(it)) }
        }
    }


    fun navigateToMatch(match: Match) = viewModelScope.launch {
        either {
            val (matchThreadEntity, matchEntity) = findSelectedMatch(match).bind()

            val mediaList = highlightParser.getMatchHighlights(
                matchHighlightsFlow.value,
                matchThreadEntity.title
            ).bind()

            toMatchList(matchThreadEntity, mediaList, match, matchEntity)
        }.fold(
            { events.emit(MatchListEvents.NavigationError(it)) },
            { events.emit(MatchListEvents.NavigateToMatchThread(it)) }
        )
    }

    private fun findSelectedMatch(newMatch: Match) = Either.catch {
        val matchThreadEntity = matchThreadListFlow.value.first { matchThread ->
            matchThread.title.contains(
                newMatch.homeTeam.name
            ) && matchThread.title.contains(
                newMatch.awayTeam.name
            )
        }
        val matchEntity = matchEntityListFlow.value.first { it.id.toString() == newMatch.id }
        Pair(matchThreadEntity, matchEntity)
    }.mapLeft { ViewError.InvalidMatchId(it.message.toString()) }

    private fun fetchMathThreadsAndHighlights() = viewModelScope.launch {
        either {
            refreshTokenIfNeeded().bind()
            val matchHighlightsRequest = async { getMatchHighlights().bind() }
            val matchThreadsRequest = async { fetchLatestMatchThreadsForTodayUseCase().bind() }
            Pair(matchHighlightsRequest.await(), matchThreadsRequest.await())
        }.mapLeft { error ->
            events.emit(MatchListEvents.Error(error.toErrorMsg()))
            savedStateHandle[KEY_MATCH_THREAD] = emptyList<MatchThreadEntity>()
            savedStateHandle[KEY_HIGHLIGHTS] = emptyList<MatchHighlight>()
        }.map { (highlights, matchThreads) ->
            savedStateHandle[KEY_MATCH_THREAD] = matchThreads
            savedStateHandle[KEY_HIGHLIGHTS] = highlights
        }
    }

    private fun DomainError.toErrorMsg(): String = when (this) {
        is NetworkError -> {
            Timber.e(this.message.toString())
            resourceProvider.getString(R.string.match_screen_error_no_internet)
        }
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }
}
