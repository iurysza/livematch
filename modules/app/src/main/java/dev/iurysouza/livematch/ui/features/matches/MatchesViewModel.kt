package dev.iurysouza.livematch.ui.features.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.domain.matches.FetchMatchesUseCase
import dev.iurysouza.livematch.domain.matches.MatchEntity
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val fetchMatches: FetchMatchesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MatchesState>(MatchesState.Loading)
    val state: StateFlow<MatchesState> = _state.asStateFlow()

    fun navigateTo() {}

    fun getLatestMatches() = viewModelScope.launch {
        either { fetchMatches().bind() }
            .map { it.toMatch() }
            .map { _state.emit(MatchesState.Success(it)) }
            .mapLeft { _state.emit(MatchesState.Error(it.toString())) }
    }

    private fun List<MatchEntity>.toMatch(): List<Match> = map { entity ->
        Match(
            id = entity.id.toString(),
            homeTeam = toTeam(entity.homeTeam, entity.score, true),
            awayTeam = toTeam(entity.awayTeam.asHomeTeam(), entity.score, false),
            startTime = entity.utcDate.format(DateTimeFormatter.ofPattern("HH:mm")),
            elapsedMinutes = calculateElapsedTime(entity),
        )
    }

    private fun calculateElapsedTime(entity: MatchEntity): String = when (entity.status) {
        "FINISHED" -> "FT"
        "IN_PLAY" -> {
            val nowInMilli: Long = Instant.now().toEpochMilli()
            val matchStartTimeInMilli = entity.utcDate.toInstant(ZoneOffset.UTC).toEpochMilli()
            // to convert timeDifference from Millis to Minutes:
            // millis -> seconds = divide by 1000
            // seconds -> minutes = divide by 60
            val diffMin = (nowInMilli - matchStartTimeInMilli) / 60000
            "$diffMin'"
        }
        else -> ""
    }

}
