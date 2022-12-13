package dev.iurysouza.livematch.matchthread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import arrow.core.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.reddit.domain.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.reddit.domain.FetchNewCommentsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
  private val eventParser: MatchEventParser,
  private val resourceProvider: ResourceProvider,
  private val fetchNewMatchComments: FetchNewCommentsUseCase,
  private val fetchMatchComments: FetchMatchCommentsUseCase,
) : ViewModel() {

  val isRefreshingState = MutableSharedFlow<Boolean>()
  private val _commentsState =
    MutableStateFlow<MatchCommentsState>(MatchCommentsState.Loading)
  val commentsState: StateFlow<MatchCommentsState> = _commentsState.asStateFlow()

  private val _state = MutableStateFlow<MatchDescriptionState>(MatchDescriptionState.Loading)
  val state: StateFlow<MatchDescriptionState> = _state.asStateFlow()

  suspend fun getLatestComments(match: MatchThread, isRefreshing: Boolean) =
    viewModelScope.launch {
      if (match.content != null && match.id != null && match.startTime != null) {
        val (matchEvents, content) = eventParser.getMatchEvents(match.content)
        _state.value = MatchDescriptionState.Success(
          matchThread = match.copy(content = content),
          matchEvents = matchEvents,
        )

        if (isRefreshing) {
          isRefreshingState.emit(true)
          fetchLatestMatchThreads(match.id)
        } else {
          _commentsState.value = MatchCommentsState.Loading
          fetchMatchThreads(match.id)
        }
          .mapLeft { mapErrorMsg(it) }
          .flatMap { eventParser.toCommentItemList(it, match.startTime) }
          .mapLeft { ViewError.CommentItemParsingError(it.toString()) }
          .flatMap { commentItemList ->
            eventParser.toCommentSectionListEvents(
              commentList = commentItemList,
              eventList = matchEvents,
              isRefreshing = isRefreshing,
            )
          }
          .map {
            it.mapIndexed { index, commentSection ->
              if (index == 0) {
                commentSection.copy(event = commentSection.event.copy(relativeTime = ""))
              } else {
                commentSection.copy(
                  event = commentSection.event.copy(
                    relativeTime = "${commentSection.event.relativeTime}'",
                  ),
                )
              }
            }
          }
          .mapLeft { ViewError.CommentSectionParsingError(it.toString()) }
          .fold(
            { _commentsState.emit(MatchCommentsState.Error(parseError(it))) },
            {
              isRefreshingState.emit(false)
              _commentsState.emit(MatchCommentsState.Success(it))
            },
          )
      }
    }

  private suspend fun fetchLatestMatchThreads(id: String) =
    either { fetchNewMatchComments(id).bind() }

  private suspend fun fetchMatchThreads(id: String) = either { fetchMatchComments(id).bind() }

  private fun mapErrorMsg(error: DomainError?): String {
    Timber.e("mapErrorMsg: $error")
    return when (error) {
      is NetworkError -> resourceProvider.getString(R.string.match_screen_error_no_internet)
      else -> resourceProvider.getString(R.string.match_screen_error_default)
    }
  }

  private fun parseError(it: ViewError): String {
    Timber.e(it.message)
    return when (it) {
      is ViewError.CommentSectionParsingError -> resourceProvider.getString(
        R.string.match_screen_error_comment_parsing,
      )
      else -> resourceProvider.getString(R.string.match_screen_error_default)
    }
  }
}
