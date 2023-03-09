package dev.iurysouza.livematch.matchthread

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import arrow.core.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.common.storage.BaseViewModel
import dev.iurysouza.livematch.matchthread.models.MatchCommentsState
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState
import dev.iurysouza.livematch.matchthread.models.MatchThread
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEffect
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEvent
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState
import dev.iurysouza.livematch.matchthread.models.ViewError
import dev.iurysouza.livematch.reddit.domain.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.reddit.domain.FetchNewCommentsUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
  private val eventParser: MatchEventParser,
  private val resourceProvider: ResourceProvider,
  private val fetchNewMatchComments: FetchNewCommentsUseCase,
  private val fetchMatchComments: FetchMatchCommentsUseCase,
) : BaseViewModel<MatchThreadViewEvent, MatchThreadViewState, MatchThreadViewEffect>() {

  override fun setInitialState(): MatchThreadViewState = MatchThreadViewState()

  override fun handleEvent(event: MatchThreadViewEvent) {
    when (event) {
      is MatchThreadViewEvent.GetLatestComments -> getLatestComments(event.match, isRefreshing = true)
      is MatchThreadViewEvent.GetMatchComments -> getLatestComments(event.match, isRefreshing = false)
    }
  }

  private fun getLatestComments(match: MatchThread, isRefreshing: Boolean) =
    viewModelScope.launch {
      if (match.content != null && match.id != null && match.startTime != null) {
        val (matchEvents, content) = eventParser.getMatchEvents(match.content)
        setState {
          copy(
            descriptionState = MatchDescriptionState.Success(
              matchThread = match.copy(content = content),
              matchEvents = matchEvents,
            ),
          )
        }
        if (isRefreshing) {
          setState { copy(isRefreshing = true) }
          fetchLatestMatchThreads(match.id)
        } else {
          setState { copy(commentSectionState = MatchCommentsState.Loading) }
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
            {
              setState { copy(commentSectionState = MatchCommentsState.Error(parseError(it))) }
            },
            {
              setState {
                copy(
                  commentSectionState = MatchCommentsState.Success(it),
                  isRefreshing = false,
                )
              }
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
