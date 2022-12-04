package dev.iurysouza.livematch.features.matchlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.core.DomainError
import dev.iurysouza.livematch.core.NetworkError
import dev.iurysouza.livematch.footballdata.domain.FetchMatchesUseCase
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.reddit.domain.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.reddit.domain.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.reddit.domain.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
import dev.iurysouza.livematch.features.matchthread.MatchHighlightParser
import dev.iurysouza.livematch.features.matchthread.ViewError
import dev.iurysouza.livematch.core.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    val isRefreshingState = MutableSharedFlow<Boolean>()
    val events = MutableSharedFlow<MatchListEvents>()

    private val matchHighlightsFlow = savedStateHandle.getStateFlow(
        key = KEY_HIGHLIGHTS,
        initialValue = emptyList<MatchHighlightEntity>()
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
    private val isSyncing = MutableStateFlow(true)

    val state = combine(_state.asStateFlow(), isSyncing.asStateFlow()) { state, isSyncing ->
        when (state) {
            is MatchListState.Error -> state
            MatchListState.Loading -> state
            is MatchListState.Success -> state.copy(isSyncing = isSyncing)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MatchListState.Loading)

    init {
        if (matchThreadListFlow.value.isEmpty() || matchHighlightsFlow.value.isEmpty()) {
            fetchRedditContent()
        }
    }

    fun getLatestMatches(isRefreshing: Boolean) = viewModelScope.launch {
        val savedMatches = matchEntityListFlow.value
        isRefreshingState.emit(isRefreshing)
        if (savedMatches.isNotEmpty() && !isRefreshing) {
            _state.emit(MatchListState.Success(savedMatches.toMatchList()))
        } else {
            if (isRefreshing) {
                fetchRedditContent()
            }
            either { fetchMatches().bind() }
                .map { matchEntityList ->
                    savedStateHandle[KEY_MATCHES] = matchEntityList
                    matchEntityList.toMatchList()
                }
                .mapLeft {
                    isRefreshingState.emit(false)
                    _state.emit(MatchListState.Error(it.toString()))
                }
                .map {
                    isRefreshingState.emit(false)
                    _state.emit(MatchListState.Success(it))
                }
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
            ) || matchThread.title.contains(
                newMatch.awayTeam.name
            )
        }
        val matchEntity = matchEntityListFlow.value.first { it.id.toString() == newMatch.id }
        Pair(matchThreadEntity, matchEntity)
    }.mapLeft { ViewError.InvalidMatchId(it.message.toString()) }

    private fun fetchRedditContent() = viewModelScope.launch {
        either {
            isSyncing.emit(true)
            refreshTokenIfNeeded().bind()
            val matchHighlightsRequest = async { getMatchHighlights().bind() }
            val matchThreadsRequest = async { fetchLatestMatchThreadsForTodayUseCase().bind() }
            Pair(matchHighlightsRequest.await(), matchThreadsRequest.await())
        }.mapLeft { error ->
            isSyncing.emit(false)
            events.emit(MatchListEvents.Error(error.toErrorMsg()))
        }.map { (highlights, matchThreads) ->
            isSyncing.emit(false)
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