package dev.iurysouza.livematch.matchday

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.components.PullToRefreshRevealComponent
import dev.iurysouza.livematch.designsystem.theme.gradientBackground
import dev.iurysouza.livematch.matchday.models.MatchDayViewState
import dev.iurysouza.livematch.matchday.models.MatchListState
import dev.iurysouza.livematch.matchday.models.MatchUiModel

@Composable
fun MatchDayScreen(
  uiState: MatchDayViewState,
  onTapItem: (MatchUiModel) -> Unit = {},
  onRefresh: () -> Unit = {},
) {
  val refreshState = rememberPullRefreshState(uiState.isRefreshing, onRefresh = onRefresh)

  Scaffold(
    modifier = Modifier
      .fillMaxHeight()
      .pullRefresh(refreshState),
    topBar = {
      TopAppBar(
        elevation = 0.dp,
        title = {
          Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            textAlign = TextAlign.Center,
          )
        },
        backgroundColor = MaterialTheme.colors.background,
        actions = {
          if (uiState.isSyncing) {
            IconButton(onClick = {}) {
              Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.icon_description),
              )
            }
          }
        },
      )
    },
  ) { paddingValues ->
    PullToRefreshRevealComponent(
      modifier = Modifier.padding(paddingValues),
      isRefreshing = uiState.isRefreshing,
      onRefresh = onRefresh,
      revealedComponentBackgroundColor = MaterialTheme.colors.secondaryVariant,
    ) {
      Box(
        Modifier
          .gradientBackground()
          .fillMaxHeight(),
      ) {
        Crossfade(targetState = uiState.matchListState, label = "MatchListStateCrossFade") { screen ->
          when (screen) {
            is MatchListState.Error -> ErrorScreen(screen.msg)
            MatchListState.Loading -> MatchDayGroupedByLeaguePlaceHolder()
            is MatchListState.Success -> MatchDayGroupedByLeague(
              modifier = Modifier,
              matchItemList = screen.matches,
              onTapMatchItem = onTapItem,
            )
          }
        }
      }
    }
  }
}


