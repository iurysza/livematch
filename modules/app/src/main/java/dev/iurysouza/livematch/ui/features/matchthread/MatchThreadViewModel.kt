package dev.iurysouza.livematch.ui.features.matchthread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import arrow.core.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.domain.adapters.DomainError
import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.domain.matchthread.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
    private val matchThreadMapper: MatchThreadMapper,
    private val resourceProvider: ResourceProvider,
    private val fetchMatchComments: FetchMatchCommentsUseCase,
) : ViewModel() {

    private val _commentsState =
        MutableStateFlow<MatchCommentsState>(MatchCommentsState.Loading)
    val commentsState: StateFlow<MatchCommentsState> = _commentsState.asStateFlow()

    private val _state = MutableStateFlow<MatchDescriptionState>(MatchDescriptionState.Loading)
    val state: StateFlow<MatchDescriptionState> = _state.asStateFlow()

    suspend fun update(match: MatchThread) = viewModelScope.launch {
        val (matchEvents, content) = matchThreadMapper.getMatchEvents(match.content)
        _state.value = MatchDescriptionState.Success(
            matchThread = match.copy(contentByteArray = content),
            matchEvents = matchEvents,
        )
        _commentsState.value = MatchCommentsState.Loading

        either { fetchMatchComments(match.id).bind() }
            .mapLeft { mapErrorMsg(it) }
            .flatMap { matchThreadMapper.toCommentItemList(it, match.startTime) }
            .mapLeft { ViewError.CommentItemParsingError(it.toString()) }
            .flatMap { matchThreadMapper.toCommentSectionListEvents(it, matchEvents) }
            .mapLeft { ViewError.CommentSectionParsingError(it.toString()) }
            .fold(
                { _commentsState.emit(MatchCommentsState.Error(parseError(it))) },
                { _commentsState.emit(MatchCommentsState.Success(it)) }
            )
    }

    private fun mapErrorMsg(error: DomainError?): String {
        Log.e("MatchThreadViewModel", "mapErrorMsg: $error")
        return when (error) {
            is NetworkError -> resourceProvider.getString(R.string.match_screen_error_no_internet)
            else -> resourceProvider.getString(R.string.match_screen_error_default)
        }
    }

    private fun parseError(it: ViewError): String {
        Log.e("MatchThreadViewModel", "parseError: $it")
        return when (it) {
            is ViewError.CommentSectionParsingError -> resourceProvider.getString(R.string.match_screen_error_comment_parsing)
            else -> resourceProvider.getString(R.string.match_screen_error_default)
        }
    }
}


