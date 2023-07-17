package dev.iurysouza.livematch.designsystem.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlin.math.roundToInt

@Composable
fun PullToReveal(
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  revealedComponent: @Composable BoxScope.(Modifier, Boolean) -> Unit,
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  refreshTriggerDistance: Dp = 120.dp,
  revealedComponentBackgroundColor: Color = MaterialTheme.colors.secondaryVariant,
) {
  val pullState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  var offset by remember { mutableStateOf(0) }
  val animatedOffset by animateIntAsState(
    targetValue = offset,
    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
    label = "PullToRevealComponent",
  )
  val density = LocalDensity.current
  val triggerPx = remember { with(density) { refreshTriggerDistance.toPx() } }
  var shouldRefresh by remember { mutableStateOf(false) }
  CompositionLocalProvider(
    LocalOverscrollConfiguration provides null,
  ) {
    SwipeRefresh(
      modifier = modifier,
      state = pullState,
      onRefresh = onRefresh,
      refreshTriggerDistance = refreshTriggerDistance,
      indicator = { state: SwipeRefreshState, _ ->
        shouldRefresh = state.indicatorOffset.roundToInt() > triggerPx
        offset = when {
          shouldRefresh -> triggerPx.roundToInt() + (state.indicatorOffset.roundToInt() * .1f).roundToInt()
          state.isRefreshing -> triggerPx.roundToInt()
          else -> state.indicatorOffset.roundToInt()
        }
      },
    ) {
      val animatedCornerRadius by animateDpAsState(
        targetValue = if (shouldRefresh || isRefreshing) 20.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "PullToRevealCornerRadius",
      )
      Box(
        Modifier.background(revealedComponentBackgroundColor),
      ) {
        val scale by animateFloatAsState(
          targetValue = if (offset > triggerPx) .95f else 1f,
          animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
          label = "PullToRevealScale",
        )
        revealedComponent(
          Modifier.height((refreshTriggerDistance + 20.dp)),
          isRefreshing,
        )
        Box(
          modifier = Modifier
            .scale(scale)
            .offset { IntOffset(x = 0, y = animatedOffset) }
            .clip(RoundedCornerShape(animatedCornerRadius))
            .fillMaxSize()
            .shadow(4.dp)
            .background(MaterialTheme.colors.surface),
        ) { content() }
      }
    }
  }
}
