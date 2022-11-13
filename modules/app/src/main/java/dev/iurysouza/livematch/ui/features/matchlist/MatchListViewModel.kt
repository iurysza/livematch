package dev.iurysouza.livematch.ui.features.matchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.adapters.models.MatchHighlight
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.domain.auth.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.domain.highlights.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.domain.matchthreads.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.ui.features.matchthread.MatchHighlightParser
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.features.matchthread.ViewError
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val highlightParser: MatchHighlightParser,
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

    private suspend fun toMatchThread(
        matchItem: MatchItem,
        highlights: List<MatchHighlight>,
        lastMatches: List<MatchThreadEntity>,
    ): Either<ViewError, MatchThread> = Either.catch {
        val matchEntity = lastMatches.find { it.id == matchItem.id }!!
        matchEntity to matchItem
    }.mapLeft {
        ViewError.InvalidMatchId(it.message.toString())
    }.flatMap { (matchEntity, matchItem) ->
        either<ViewError, MatchThread> {
            MatchThread(
                id = matchEntity.id,
                title = matchItem.title,
                competition = matchItem.competition,
                contentByteArray = matchEntity.content.toByteArray(),
                startTime = matchEntity.createdAt,
                mediaList = highlightParser.getMatchHighlights(highlights, matchItem.title).bind()
            )
        }
    }

    fun navigateTo(matchItem: MatchItem) = viewModelScope.launch {
        toMatchThread(
            matchItem = matchItem,
            highlights = lastHighlights,
            lastMatches = lastMatches
        ).fold(
            { events.emit(MatchListEvents.NavigationError(it)) },
            { events.emit(MatchListEvents.NavigateToMatchThread(it)) }
        )
    }

    private fun DomainError.toErrorMsg(): String = when (this) {
        is NetworkError -> {
            Timber.e(this.message.toString())
            resourceProvider.getString(R.string.match_screen_error_no_internet)
        }
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }
}

private const val TITLE_PATTERN = """Match Thread: (.*) \| (.*)"""

private fun List<MatchThreadEntity>.toMatchItem(
    enabledCompetitions: List<String>,
): List<MatchItem> = mapNotNull { match ->

    val regexMatch = TITLE_PATTERN.toRegex().find(match.title) ?: return@mapNotNull null
    val (title, competition) = regexMatch.destructured

    MatchItem(match.id, title, competition)
}
//    .filter { enabledCompetitions.any { competition -> it.competition.contains(competition) } }
    .sortedBy { it.competition }
