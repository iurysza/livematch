package dev.iurysouza.livematch.ui.features.matchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.adapters.models.MatchThreadEntity
import dev.iurysouza.livematch.domain.auth.RefreshTokenIfNeededUseCase
import dev.iurysouza.livematch.domain.matchlist.FetchLatestMatchThreadsForTodayUseCase
import dev.iurysouza.livematch.ui.features.matchthread.InvalidMatchId
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val latestMatchThreadsForToday: FetchLatestMatchThreadsForTodayUseCase,
    private val refreshTokenIfNeeded: RefreshTokenIfNeededUseCase,
) : ViewModel() {

    val events = MutableSharedFlow<MatchListEvents>()

    private val _state = MutableStateFlow<MatchListState>(MatchListState.Loading)
    val state: StateFlow<MatchListState> = _state.asStateFlow()

    private var lastMatches = emptyList<MatchThreadEntity>()

    fun getMachList() = viewModelScope.launch {
        either {
            refreshTokenIfNeeded().bind()
            latestMatchThreadsForToday().bind()
        }.mapLeft { error ->
            lastMatches = emptyList()
            error.toErrorMsg()
        }.map { matchList ->
            lastMatches = matchList
            matchList.toMatchItem()
        }.fold(
            { _state.emit(MatchListState.Error(it)) },
            { _state.emit(MatchListState.Success(it)) }
        )
    }

    fun navigateTo(matchItem: MatchItem) = viewModelScope.launch {
        catch {
            val matchEntity = lastMatches.find { it.id == matchItem.id }!!
            matchEntity to matchItem
        }.mapLeft {
            InvalidMatchId
        }.map { (matchEntity, matchItem) ->
            MatchThread(
                id = matchEntity.id,
                title = matchItem.title,
                competition = matchItem.competition,
                contentByteArray = matchEntity.contentHtml.toByteArray()
            )
        }.fold(
            { events.emit(MatchListEvents.NavigationError(it)) },
            { events.emit(MatchListEvents.NavigateToMatchThread(it)) }
        )
    }

    private fun List<MatchThreadEntity>.toMatchItem(): List<MatchItem> = mapNotNull { match ->
        runCatching {
            val (title, subtitle) = match.title
                .replace("Match Thread:", "")
                .split("|")
            MatchItem(match.id, title, subtitle)
        }
            .onFailure { Log.e("LiveMatch", "Error parsing match thread", it) }
            .getOrNull()
    }.sortedBy { it.competition }


    private fun DomainError.toErrorMsg(): String = when (this) {
        is NetworkError -> resourceProvider.getString(R.string.match_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.match_screen_error_default)
    }
}
