package dev.iurysouza.livematch.matchthread.ui.screens.matchthread

import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import arrow.core.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.common.DomainError
import dev.iurysouza.livematch.common.MVIViewModel
import dev.iurysouza.livematch.common.NetworkError
import dev.iurysouza.livematch.common.ResourceProvider
import dev.iurysouza.livematch.matchthread.MatchEventParser
import dev.iurysouza.livematch.matchthread.R
import dev.iurysouza.livematch.matchthread.models.MatchCommentsState
import dev.iurysouza.livematch.matchthread.models.MatchDescriptionState
import dev.iurysouza.livematch.matchthread.models.MatchEvent
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
import dev.iurysouza.livematch.reddit.domain.MatchParams
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
      is MatchThreadViewEvent.GetLatestComments -> getMatchThreadData(event.params, isRefreshing = true)
      is MatchThreadViewEvent.GetMatchComments -> getMatchThreadData(event.params, isRefreshing = false)
    }
  }

  private fun getMatchThreadData(
    params: MatchThreadParams,
    isRefreshing: Boolean,
  ) {
    val (matchEvents, content) = eventParser.getMatchEvents(params.content)
    viewModelScope.launch { fetchMatchComments(isRefreshing, params, matchEvents) }
    viewModelScope.launch { fetchMatchHighlights(params, content) }
  }

  private suspend fun fetchMatchComments(
    isRefreshing: Boolean,
    params: MatchThreadParams,
    matchEvents: List<MatchEvent>,
  ) = either {
    if (isRefreshing) {
      setState { copy(isRefreshing = true) }
      fetchNewMatchComments.execute(MatchId(params.id)).bind()
    } else {
      setState { copy(commentSectionState = MatchCommentsState.Loading) }
      fetchMatchComments.execute(MatchId(params.id)).bind()
    }
  }
    .mapLeft { mapErrorMsg(it) }
    .flatMap { eventParser.createEventSectionsWithComments(it, params.startTime, matchEvents, isRefreshing) }
    .mapLeft { ViewError.CommentSectionParsingError(it.toString()) }
    .fold(
      {
        setState {
          copy(
            commentSectionState = MatchCommentsState.Error(parseError(it)),
            isRefreshing = false,
          )
        }
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

  private suspend fun fetchMatchHighlights(
    params: MatchThreadParams,
    content: String,
  ) = either {
    setState { copy(descriptionState = MatchDescriptionState.Loading) }
    fetchMatchHighlightsUseCase.execute(
      MatchParams(
        pages = 1,
        homeTeam = params.homeTeam,
        awayTeam = params.awayTeam,
      ),
    ).bind()
  }
    .mapLeft { mapErrorMsg(it) }
    .map { it.toUi() }
    .fold(
      { error ->
        setState { copy(descriptionState = MatchDescriptionState.Error(error)) }
      },
      { mediaList ->
        setState {
          copy(descriptionState = MatchDescriptionState.Success(eventParser.parseContent(content), mediaList))
        }
      },
    )

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
