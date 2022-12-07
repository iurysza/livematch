package dev.iurysouza.livematch.matchlist.betterarchitecture

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.common.storage.BaseViewModel
import dev.iurysouza.livematch.footballdata.domain.FetchMatchesUseCase
import dev.iurysouza.livematch.matchlist.Match
import dev.iurysouza.livematch.matchlist.MatchListState
import dev.iurysouza.livematch.matchlist.Team
import dev.iurysouza.livematch.reddit.domain.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.reddit.domain.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.reddit.domain.MatchHighlightParserUseCase
import dev.iurysouza.livematch.reddit.domain.RefreshTokenIfNeededUseCase
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class BetterMatchListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider,
    private val fetchMatches: FetchMatchesUseCase,
    private val highlightParser: MatchHighlightParserUseCase,
    private val getMatchHighlights: GetMatchHighlightsUseCase,
    private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
    private val fetchLatestMatchThreadsForToday: FetchLatestMatchThreadsForTodayUseCase,
) : BaseViewModel<MatchListViewEvent, MatchListViewState, MatchListViewEffect>() {

    override fun setInitialState(): MatchListViewState = MatchListViewState()

    override fun handleEvent(event: MatchListViewEvent) = when (event) {
        MatchListViewEvent.GetLatestMatches -> getLatestMatches()
        MatchListViewEvent.Refresh -> onRefresh()
        is MatchListViewEvent.NavigateToMatch -> handleNavigation(event)
    }

    private fun getLatestMatches() {
        viewModelScope.launch {
            setState {
                copy(
                    matchListState = MatchListState.Loading,
                    isSyncing = false
                )
            }

            delay(300)

            setState {
                copy(
                    matchListState = MatchListState.Success(
                        matches = listOf(
                            Match(
                                id = "",
                                homeTeam = Team(
                                    crestUrl = "https://crests.football-data.org/770.svg",
                                    name = "England",
                                    isHomeTeam = false,
                                    isAhead = true,
                                    score = "1"
                                ),
                                awayTeam = Team(
                                    crestUrl = "https://crests.football-data.org/776.svg",
                                    name = "Wales",
                                    isHomeTeam = false,
                                    isAhead = false,
                                    score = "0"
                                ),
                                startTime = "16:00",
                                elapsedMinutes = "FT"
                            )
                        )
                    ), isSyncing = false)
            }
            delay(300)
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            setState {
                copy(isSyncing = true)
            }

            delay(300)

            setState {
                copy(isSyncing = false)
            }

        }
    }

    private fun handleNavigation(event: MatchListViewEvent.NavigateToMatch) {
        setEffect { MatchListViewEffect.NavigationError("No Match yet") }
    }
}


private const val KEY_MATCH_THREAD = "matchThreads"
private const val KEY_HIGHLIGHTS = "highlights"
private const val KEY_MATCHES = "matches"

