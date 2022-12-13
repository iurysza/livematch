package dev.iurysouza.livematch.common.storage

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<
  Event : ViewEvent,
  UiState : ViewState,
  Effect : ViewSideEffect,
  > : ViewModel() {

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
    viewModelScope.launch { _event.emit(event) }
  }

  /**
   * Updates the current view state using the provided reducer function.
   *
   * @param reducer a function that takes the current view state and returns a new view state
   */
  protected fun setState(reducer: UiState.() -> UiState) {
    val newState = viewState.value.reducer()
    _viewState.value = newState
  }

  private fun subscribeToEvents() {
    viewModelScope.launch {
      _event.collect {
        handleEvent(it)
      }
    }
  }

  abstract fun handleEvent(event: Event)

  protected fun setEffect(builder: () -> Effect) {
    val effectValue = builder()
    viewModelScope.launch { _effect.emit(effectValue) }
  }
}

interface ViewState

interface ViewEvent

interface ViewSideEffect
