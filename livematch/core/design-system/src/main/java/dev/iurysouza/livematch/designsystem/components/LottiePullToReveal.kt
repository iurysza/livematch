package dev.iurysouza.livematch.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun LottiePullToReveal(
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  content: @Composable () -> Unit,
  lottieAsset: LottieAsset,
  modifier: Modifier = Modifier,
  revealedComponentBackgroundColor: Color = MaterialTheme.colors.secondaryVariant,
) {
  PullToReveal(
    modifier = modifier,
    revealedComponentBackgroundColor = revealedComponentBackgroundColor,
    isRefreshing = isRefreshing,
    onRefresh = onRefresh,
    content = content,
    revealedComponent = { animationModifier, shouldAnimate ->
      LottieAnimationComposable(
        modifier = animationModifier,
        isRefreshing = shouldAnimate,
        assetName = lottieAsset.name,
      )
    },
  )
}

@Suppress("MagicNumber")
@Composable
fun LottieAnimationComposable(
  isRefreshing: Boolean,
  assetName: String,
  modifier: Modifier = Modifier,
) {
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
      modifier = Modifier.align(Alignment.Center),
    )
  }
}

sealed class LottieAsset(val name: String) {
  object FootballFans : LottieAsset("football-fans.json")
}
