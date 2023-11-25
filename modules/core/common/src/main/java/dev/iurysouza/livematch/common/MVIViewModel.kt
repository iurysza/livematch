package dev.iurysouza.livematch.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class MVIViewModel<
  Event : ViewEvent,
  UiState : ViewState,
  Effect : ViewSideEffect,
  >(
  private val logLevel: Int = MVILogLevel.NONE,
) : ViewModel() {

  private val initialState: UiState by lazy { setInitialState() }
  abstract fun setInitialState(): UiState

  private val _viewState: MutableState<UiState> = mutableStateOf(initialState)

  /**
   * A [State] object that represents the current state of the ViewModel. This can be observed
   * by collecting the `viewState` flow.
   */
  val viewState: State<UiState> = _viewState

  private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

  private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()

  /**
   * A [SharedFlow] that emits the side effects of the ViewModel. This can be observed by
   * collecting the `effect` flow.
   */
  val effect = _effect.asSharedFlow()

  init {
    subscribeToEvents()
  }

  /**
   * Sets the event that is emitted by the UI. This should be called by the UI whenever a new
   * event occurs that should be handled by the ViewModel.
   *
   * @param event the event that is emitted by the UI
   */
  fun setEvent(event: Event) {
    if ((logLevel and MVILogLevel.EVENT) == MVILogLevel.EVENT || logLevel == MVILogLevel.ALL) {
      Timber.v("Event Set: $event")
    }
    viewModelScope.launch { _event.emit(event) }
  }

  /**
   * Updates the current view state using the provided reducer function.
   *
   * @param reducer a function that takes the current view state and returns a new view state
   */
  protected fun setState(reducer: UiState.() -> UiState) {
    val stateLoggingEnabled = (logLevel and MVILogLevel.STATE) == MVILogLevel.STATE || logLevel == MVILogLevel.ALL
    if (stateLoggingEnabled) {
      Timber.v("State Updated: Previous State:\n${_viewState.value.prettyPrint()}")
    }
    val newState = viewState.value.reducer()
    if (stateLoggingEnabled) {
      Timber.v("State Update - New state:\n${newState.prettyPrint()}")
    }
    _viewState.value = newState
  }

  private fun Any.prettyPrint() = toString().replace(",", "\n")

  private fun subscribeToEvents() {
    viewModelScope.launch {
      _event.collect {
        handleEvent(it)
      }
    }
  }

  open fun handleEvent(event: Event) {
    if ((logLevel and MVILogLevel.EVENT) == MVILogLevel.EVENT || logLevel == MVILogLevel.ALL) {
      Timber.v("Event Handled: $event")
    }
  }

  protected fun setEffect(builder: () -> Effect) {
    val effectValue = builder()
    viewModelScope.launch {
      if ((logLevel and MVILogLevel.EFFECT) == MVILogLevel.EFFECT || logLevel == MVILogLevel.ALL) {
        Timber.v("Effect Set: $effectValue")
      }
      _effect.emit(effectValue)
    }
  }
}

interface ViewState

interface ViewEvent

interface ViewSideEffect

object MVILogLevel {
  const val NONE = 0
  const val EVENT = 1 shl 0 // 1
  const val STATE = 1 shl 1 // 2
  const val EFFECT = 1 shl 2 // 4
  const val ALL = EVENT or STATE or EFFECT // 7
}
