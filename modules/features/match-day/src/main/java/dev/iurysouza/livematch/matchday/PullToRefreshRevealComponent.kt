package dev.iurysouza.livematch.matchday

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@ExperimentalFoundationApi
@Composable
fun PullToRefreshRevealComponent(
  modifier: Modifier = Modifier,
  refreshTriggerDistance: Dp = 120.dp,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  revealedComponentBackgroundColor: Color = Color(0xFF323247),
  revealedComponent: @Composable BoxScope.(Modifier, Boolean) -> Unit = { animationModifier, shouldAnimate ->
    LottieAnimationComp(
      modifier = animationModifier,
      isRefreshing = shouldAnimate,
      assetName = "soccer-fans.json",
    )
  },
  content: @Composable () -> Unit,
) {

  val pullState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  var offset by remember { mutableStateOf(0) }
  val animatedOffset by animateIntAsState(
    targetValue = offset,
    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
    label = "FancyPullToRefreshOffset",
  )
  val density = LocalDensity.current
  val triggerPx = remember { with(density) { refreshTriggerDistance.toPx() } }

  CompositionLocalProvider(
    LocalOverscrollConfiguration provides null,
  ) {
    SwipeRefresh(
      modifier = modifier,
      state = pullState,
      onRefresh = onRefresh,
      refreshTriggerDistance = refreshTriggerDistance,
      indicator = { state: SwipeRefreshState, _ ->
        val shouldRefresh = state.indicatorOffset.roundToInt() > triggerPx

        offset = when {
          shouldRefresh -> triggerPx.roundToInt() + (state.indicatorOffset.roundToInt() * .1f).roundToInt()
          state.isRefreshing -> triggerPx.roundToInt()
          else -> state.indicatorOffset.roundToInt()
        }
      },
    ) {
      Box(
        modifier.background(revealedComponentBackgroundColor),
      ) {
        val scale by animateFloatAsState(
          targetValue = if (offset > triggerPx) .95f else 1f,
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
          ),
          label = "FancyPullToRefreshScale",
        )
        val animatedCornerRadius by animateDpAsState(
          targetValue = if (isRefreshing) 20.dp else 0.dp,
          label = "FancyPullToRefreshCornerRadius",
        )
        revealedComponent(
          modifier.height((refreshTriggerDistance + 20.dp)),
          isRefreshing,
        )
        Box(
          modifier = modifier
            .scale(scale)
            .offset { IntOffset(x = 0, y = animatedOffset) }
            .clip(RoundedCornerShape(topStart = animatedCornerRadius, topEnd = animatedCornerRadius))
            .fillMaxSize()
            .shadow(4.dp)
            .background(MaterialTheme.colors.surface),
        ) {
          content()
        }
      }
    }
  }
}

@Composable
fun LottieAnimationComp(modifier: Modifier, isRefreshing: Boolean, assetName: String = "") {
  val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetName))
  val shouldRefresh = remember { mutableStateOf(false) }
  LaunchedEffect(isRefreshing) {
    if (!isRefreshing) delay(150)
    shouldRefresh.value = isRefreshing
  }
  Box(
    modifier = modifier.fillMaxWidth(),
  ) {
    LottieAnimation(
      composition = composition,
      reverseOnRepeat = true,
      isPlaying = shouldRefresh.value,
      restartOnPlay = true,
      iterations = 99,
      modifier = modifier.align(Alignment.Center),
    )
  }
}
