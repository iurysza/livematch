package dev.iurysouza.livematch.ui.features.matchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.NetworkError
import dev.iurysouza.livematch.domain.auth.AuthUseCase
import dev.iurysouza.livematch.domain.matchlist.MatchListUseCase
import dev.iurysouza.livematch.domain.matchlist.MatchThreadEntity
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val matchListUseCase: MatchListUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    val state = MutableStateFlow<MatchListState>(MatchListState.Loading)

    init {
        getMachList()
    }

    private fun getMachList() = viewModelScope.launch {
        either {
            authUseCase.refreshTokenIfNeeded().bind()
            matchListUseCase.getMatches().bind()
        }.map { it.parseText() }.fold(
            ifLeft = { error ->
                Log.e("PostsViewModel", "error: $error")
                state.emit(MatchListState.Error(mapErrorMsg(error)))
            },
            ifRight = { matchList ->
                state.emit(MatchListState.Success(matchList))
            }
        )
    }

    private fun List<MatchThreadEntity>.parseText() = map { match ->
        val index = match.contentHtml.indexOf("<p><a href=\"#icon-net-big\"></a>")
        val finalContent = match.contentHtml.take(index)
        match.copy(contentHtml = finalContent)
    }

    private fun mapErrorMsg(error: DomainError?): String = when (error) {
        is NetworkError -> resourceProvider.getString(R.string.post_screen_error_no_internet)
        else -> resourceProvider.getString(R.string.post_screen_error_default)
    }
}


