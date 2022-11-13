package dev.iurysouza.livematch.ui.features.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.iurysouza.livematch.data.network.footballdata.models.AwayTeam
import dev.iurysouza.livematch.data.network.footballdata.models.HomeTeam
import dev.iurysouza.livematch.data.network.footballdata.models.Score
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
import timber.log.Timber

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
            .mapLeft { _state.emit(MatchesState.Error(it.toString()))  }
    }

    private fun List<MatchEntity>.toMatch(): List<Match> = map { entity ->
        Match(
            id = entity.id.toString(),
            homeTeam = toTeam(entity.homeTeam, entity.score, true),
            awayTeam = toTeam(asHomeTeam(entity.awayTeam), entity.score, false),
            startTime = entity.utcDate.format(DateTimeFormatter.ofPattern("HH:mm")),
            elapsedMinutes = calculateElapsedTime(entity),
        )
    }

    private fun calculateElapsedTime(entity: MatchEntity): String = when (entity.status) {
        "FINISHED" -> "FT"
        "IN_PLAY" -> {
            val now: Long = Instant.now().toEpochMilli()
            val matchTime = Instant.ofEpochSecond(
                entity.utcDate.toEpochSecond(ZoneOffset.UTC))
                .toEpochMilli()
            val diffMs: Long = now - matchTime
            val diffSec = diffMs / 1000
            "${diffSec / 60}'"
        }
        else -> ""
    }

    private fun asHomeTeam(awayTeam: AwayTeam) = HomeTeam(
        crest = awayTeam.crest,
        id = awayTeam.id,
        name = awayTeam.name,
        shortName = awayTeam.shortName,
        tla = awayTeam.tla
    )

    private fun toTeam(homeTeam: HomeTeam, score: Score, isHome: Boolean): Team {
        val validHomeScore = if (score.fullTime == null) {
            score.halfTime?.home?.toString() ?: "0"
        } else {
            score.fullTime.home?.toString() ?: "0"
        }

        val validAwayScore = if (score.fullTime == null) {
            score.halfTime?.away?.toString() ?: "0"
        } else {
            score.fullTime.away?.toString() ?: "0"
        }

        val teamScore = if (isHome) {
            validHomeScore
        } else {
            validAwayScore
        }

        val isHomeAhead = validHomeScore.toInt() > validAwayScore.toInt()
        val isAwayAhead = validAwayScore.toInt() > validHomeScore.toInt()

        val isTeamAhead = if (isHome) isHomeAhead else isAwayAhead

        return Team(
            crestUrl = homeTeam.crest,
            name = homeTeam.shortName ?: homeTeam.name!!,
            score = teamScore,
            isAhead = isTeamAhead,
            isHomeTeam = isHome,
        )
    }

}
