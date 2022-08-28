package dev.iurysouza.livematch.ui.features.matchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Companion.catch
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.adapters.MatchThreadEntity
import dev.iurysouza.livematch.domain.auth.AuthUseCase
import dev.iurysouza.livematch.domain.matchlist.MatchListUseCase
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val matchListUseCase: MatchListUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _effects = MutableStateFlow<MatchListEffects>(MatchListEffects.Idle)
    val effects: StateFlow<MatchListEffects> = _effects.asStateFlow()

    private val _state = MutableStateFlow<MatchListState>(MatchListState.Loading)
    val state: StateFlow<MatchListState> = _state.asStateFlow()

    private var lastMatches = emptyList<MatchThreadEntity>()

    fun getMachList() = viewModelScope.launch {
        either {
            authUseCase.refreshTokenIfNeeded().bind()
            matchListUseCase.getMatches().bind()
        }.map {
            lastMatches = it
            it.toMatchItem()
        }.fold(
            ifLeft = { error ->
                lastMatches = emptyList()
                Log.e("PostsViewModel", "error: $error")
                _state.emit(MatchListState.Error(mapErrorMsg(error)))
            },
            ifRight = { matchList ->
                _state.emit(MatchListState.Success(matchList))
            }
        )
    }

    fun navigateTo(matchItem: MatchItem) {
        viewModelScope.launch {
            catch {
                val currentState = _state.value as MatchListState.Success
                val match = lastMatches.find { it.id == matchItem.id }!!
                match to currentState.matchList.find { it.id == matchItem.id }!!
            }.mapLeft {
                InvalidMatchId
            }.map { (matchEntity, matchItem) ->
                MatchThread(
                    title = matchItem.title,
                    competition = matchItem.competition,
                    matchDescriptionHtml = matchEntity.contentHtml,
                    commentList = emptyList()
                )
            }.fold(
                { error ->
                    Log.e("LiveMatch", "error: $error")
                    _effects.emit(MatchListEffects.NavigationError(error))
                }
            ) { matchThread ->
                _effects.emit(MatchListEffects.NavigateToMatchThread(matchThread))
            }
        }
    }

    private fun List<MatchThreadEntity>.toMatchItem(): List<MatchItem> = mapNotNull { match ->
        runCatching {
            val (title, subtitle) = match.title
                .replace("Match Thread:", "")
                .split("|")
            MatchItem(match.id, title, subtitle)
        }.getOrNull()
    }.sortedBy { it.competition }


    private fun mapErrorMsg(error: DomainError?): String = when (error) {
        is NetworkError -> resourceProvider.getString(R.string.post_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.post_screen_error_default)
    }
}
