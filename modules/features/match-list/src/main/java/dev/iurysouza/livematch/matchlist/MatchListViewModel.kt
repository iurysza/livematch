package dev.iurysouza.livematch.matchlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.footballdata.domain.FetchMatchesUseCase
import dev.iurysouza.livematch.footballdata.domain.models.MatchEntity
import dev.iurysouza.livematch.reddit.domain.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.reddit.domain.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.reddit.domain.MatchHighlightParserUseCase
import dev.iurysouza.livematch.reddit.domain.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.reddit.domain.models.MatchHighlightEntity
import dev.iurysouza.livematch.reddit.domain.models.MatchThreadEntity
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

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider,
    private val fetchMatches: FetchMatchesUseCase,
    private val highlightParser: MatchHighlightParserUseCase,
    private val getMatchHighlights: GetMatchHighlightsUseCase,
    private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
    private val fetchLatestMatchThreadsForTodayUseCase: FetchLatestMatchThreadsForTodayUseCase,
) : ViewModel() {

    private val isRefreshingState = MutableStateFlow(true)
    private val matchList = MutableStateFlow<MatchListState>(MatchListState.Loading)
    private val savedHighlights = savedStateHandle.getStateFlow(
        key = KEY_HIGHLIGHTS,
        initialValue = emptyList<MatchHighlightEntity>()
    )
    private val savedMatchThreads = savedStateHandle.getStateFlow(
        key = KEY_MATCH_THREAD,
        initialValue = emptyList<MatchThreadEntity>()
    )
    private val savedMatches = savedStateHandle.getStateFlow(
        key = KEY_MATCHES,
        initialValue = emptyList<MatchEntity>()
    )

    val uiEvent = MutableSharedFlow<MatchListEvents>()
    val uiModel =
        combine(matchList.asStateFlow(), isRefreshingState.asStateFlow()) { state, isSyncing ->
            UIModel(state, isSyncing)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UIModel())

    init {
        if (savedMatchThreads.value.isEmpty() || savedHighlights.value.isEmpty()) {
            fetchRedditContent()
        }
    }

    fun getLatestMatches(isRefreshing: Boolean) = viewModelScope.launch {
        val savedMatches = savedMatches.value
        if (savedMatches.isNotEmpty() && !isRefreshing) {
            matchList.emit(MatchListState.Success(savedMatches.toMatchList()))
        } else {
            if (isRefreshing) {
                fetchRedditContent()
            }
            either { fetchMatches().bind() }.map { matchEntityList ->
                savedStateHandle[KEY_MATCHES] = matchEntityList
                matchEntityList.toMatchList()
            }.mapLeft {
                isRefreshingState.emit(false)
                matchList.emit(matchListStateError())
            }.map {
                isRefreshingState.emit(true)
                matchList.emit(MatchListState.Success(it))
            }
        }
    }

    private fun matchListStateError(): MatchListState.Error = MatchListState.Error(
        resourceProvider.getString(R.string.match_screen_error_no_internet)
    )

    fun navigateToMatch(match: Match) = viewModelScope.launch {
        either {
            val (matchThreadEntity, matchEntity) = findSelectedMatch(match).bind()
            val mediaList = highlightParser.getMatchHighlights(
                savedHighlights.value,
                matchThreadEntity.title
            ).bind()
            buildMatchThreadWith(matchThreadEntity, mediaList, match, matchEntity)
        }.fold(
            { uiEvent.emit(MatchListEvents.NavigationError(it.toString())) },
            { uiEvent.emit(MatchListEvents.NavigateToMatchThread(it)) })
    }

    private fun findSelectedMatch(selectedMatch: Match) = Either.catch {
        val matchThreadEntity = savedMatchThreads.value.first { matchThread ->
            val title = matchThread.title
            title.contains(selectedMatch.homeTeam.name) || title.contains(selectedMatch.awayTeam.name)
        }
        val matchEntity = savedMatches.value.first { it.id.toString() == selectedMatch.id }
        Pair(matchThreadEntity, matchEntity)
    }.mapLeft { ViewError.InvalidMatchId(it.message.toString()) }

    private fun fetchRedditContent() = viewModelScope.launch {
        either {
            isRefreshingState.emit(true)
            refreshTokenIfNeeded().bind()
            val matchHighlightsRequest = async { getMatchHighlights().bind() }
            val matchThreadsRequest = async { fetchLatestMatchThreadsForTodayUseCase().bind() }
            Pair(matchHighlightsRequest.await(), matchThreadsRequest.await())
        }.mapLeft { error ->
            isRefreshingState.emit(false)
            uiEvent.emit(MatchListEvents.Error(error.toErrorMsg()))
        }.map { (highlights, matchThreads) ->
            isRefreshingState.emit(false)
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

private const val KEY_MATCH_THREAD = "matchThreads"
private const val KEY_HIGHLIGHTS = "highlights"
private const val KEY_MATCHES = "matches"
