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
import dev.iurysouza.livematch.domain.matches.FetchMatchesUseCase
import dev.iurysouza.livematch.domain.matches.MatchEntity
import dev.iurysouza.livematch.domain.matchthreads.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.ui.features.matchthread.Competition
import dev.iurysouza.livematch.ui.features.matchthread.MatchHighlightParser
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.features.matchthread.ViewError
import dev.iurysouza.livematch.util.ResourceProvider
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val fetchMatches: FetchMatchesUseCase,
    private val highlightParser: MatchHighlightParser,
    private val getMatchHighlights: GetMatchHighlightsUseCase,
    private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
    private val fetchLatestMatchThreadsForTodayUseCase: FetchLatestMatchThreadsForTodayUseCase,
) : ViewModel() {

    val events = MutableSharedFlow<MatchListEvents>()

    private val _state = MutableStateFlow<MatchesState>(MatchesState.Loading)
    val state: StateFlow<MatchesState> = _state.asStateFlow()

    private var lastHighlights = emptyList<MatchHighlight>()
    private var matchItems = emptyList<MatchItem>()
    private var matches = emptyList<MatchEntity>()
    private var lastMatches = emptyList<MatchThreadEntity>()
    private val enabledCompetitions = listOf(
        "English Premier League",
        "Italian Seria A",
        "LaLiga",
        "Ligue 1",
        "German Bundesliga",
    )

    init {
        getMachList()
    }

    fun getLatestMatches() = viewModelScope.launch {
        either { fetchMatches().bind() }
            .map {
                matches = it
                it.toMatch()
            }
            .map { _state.emit(MatchesState.Success(it)) }
            .mapLeft { _state.emit(MatchesState.Error(it.toString())) }
    }

    private fun List<MatchEntity>.toMatch(): List<Match> = map { entity ->
        Match(
            id = entity.id.toString(),
            homeTeam = toTeam(entity.homeTeam, entity.score, true),
            awayTeam = toTeam(entity.awayTeam.asHomeTeam(), entity.score, false),
            startTime = entity.utcDate.format(DateTimeFormatter.ofPattern("HH:mm")),
            elapsedMinutes = when (entity.status) {
                "FINISHED" -> "FT"
                "IN_PLAY" -> {
                    val nowInMilli: Long = Instant.now().toEpochMilli()
                    val matchStartTimeInMilli =
                        entity.utcDate.toInstant(ZoneOffset.UTC).toEpochMilli()
                    // to convert timeDifference from Millis to Minutes:
                    // millis -> seconds = divide by 1000
                    // seconds -> minutes = divide by 60
                    val diffMin = (nowInMilli - matchStartTimeInMilli) / 60000
                    "$diffMin'"
                }
                else -> ""
            }
        )
    }


    private fun getMachList() = viewModelScope.launch {
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
            matchItems = matchList.toMatchItem(enabledCompetitions)
        }
    }

    private suspend fun toMatchThread(
        matchItem: MatchItem,
        highlights: List<MatchHighlight>,
        lastMatches: List<MatchThreadEntity>,
        match: Match,
        entity: MatchEntity,
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
                content = matchEntity.content,
                startTime = matchEntity.createdAt,
                mediaList = highlightParser.getMatchHighlights(highlights, matchItem.title).bind(),
                homeTeam = match.homeTeam,
                awayTeam = match.awayTeam,
                refereeList = entity.referees.mapNotNull { it.name },
                competition = Competition(
                    id = entity.competition.id!!,
                    name = entity.competition.name!!,
                    emblemUrl = entity.competition.emblem!!,
                )
            )
        }
    }

    fun navigateToMatch(newMatch: Match) = viewModelScope.launch {
        either {
            val (selectedMatch, entity) = Either.catch {
                val matchEntity = matches.first { it.id.toString() == newMatch.id }
                val selectedMatch: MatchItem =
                    matchItems.first { it.title.contains(newMatch.homeTeam.name) }
                selectedMatch to matchEntity
            }.mapLeft { ViewError.InvalidMatchId(it.message.toString()) }.bind()
            toMatchThread(selectedMatch, lastHighlights, lastMatches, newMatch, entity).bind()
        }.fold(
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
    .filter { enabledCompetitions.any { competition -> it.competition.contains(competition) } }
    .sortedBy { it.competition }
