package dev.iurysouza.livematch.matchthread

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import arrow.core.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MVIViewModel
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.matchthread.models.MatchCommentsState
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState
import dev.iurysouza.livematch.matchthread.models.MatchThreadParams
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEffect
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewEvent
import dev.iurysouza.livematch.matchthread.models.MatchThreadViewState
import dev.iurysouza.livematch.matchthread.models.ViewError
import dev.iurysouza.livematch.matchthread.models.toUi
import dev.iurysouza.livematch.reddit.domain.FetchMatchCommentsUseCase
import dev.iurysouza.livematch.reddit.domain.FetchNewCommentsUseCase
import dev.iurysouza.livematch.reddit.domain.GetMatchHighlightsUseCase
import dev.iurysouza.livematch.reddit.domain.MatchId
import dev.iurysouza.livematch.reddit.domain.MatchTitle
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MatchThreadViewModel @Inject constructor(
  private val eventParser: MatchEventParser,
  private val resourceProvider: ResourceProvider,
  private val fetchNewMatchComments: FetchNewCommentsUseCase,
  private val fetchMatchComments: FetchMatchCommentsUseCase,
  private val fetchMatchHighlightsUseCase: GetMatchHighlightsUseCase,
) : MVIViewModel<MatchThreadViewEvent, MatchThreadViewState, MatchThreadViewEffect>() {

  override fun setInitialState(): MatchThreadViewState = MatchThreadViewState()

  override fun handleEvent(event: MatchThreadViewEvent) {
    super.handleEvent(event)
    when (event) {
      is MatchThreadViewEvent.GetLatestComments -> getLatestComments(event.params, isRefreshing = true)
      is MatchThreadViewEvent.GetMatchComments -> getLatestComments(event.params, isRefreshing = false)
    }
  }

  private fun getLatestComments(
    params: MatchThreadParams,
    isRefreshing: Boolean,
  ) {
    val (matchEvents, content) = eventParser.getMatchEvents(params.content)
    viewModelScope.launch {
      either {
        if (isRefreshing) {
          setState { copy(isRefreshing = true) }
          fetchNewMatchComments.execute(MatchId(params.id)).bind()
        } else {
          setState { copy(commentSectionState = MatchCommentsState.Loading) }
          fetchMatchComments.execute(MatchId(params.id)).bind()
        }
      }
        .mapLeft { mapErrorMsg(it) }
        .flatMap { eventParser.createEventSecionsWithComments(it, params.startTime, matchEvents, isRefreshing) }
        .mapLeft { ViewError.CommentSectionParsingError(it.toString()) }
        .fold(
          {
            setState { copy(commentSectionState = MatchCommentsState.Error(parseError(it))) }
          },
          {
            setState {
              copy(
                commentSectionState = MatchCommentsState.Success(it.toImmutableList()),
                isRefreshing = false,
              )
            }
          },
        )
    }
    viewModelScope.launch {
      setState { copy(descriptionState = MatchDescriptionState.Loading) }
      either {
        fetchMatchHighlightsUseCase.execute(MatchTitle(params.title)).bind()
      }.mapLeft { mapErrorMsg(it) }
        .map { it.toUi() }
        .fold(
          { error ->
            setState { copy(descriptionState = MatchDescriptionState.Error(error)) }
          },
          { mediaList ->
            setState {
              copy(descriptionState = MatchDescriptionState.Success(content, mediaList))
            }
          },
        )
    }
  }

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
