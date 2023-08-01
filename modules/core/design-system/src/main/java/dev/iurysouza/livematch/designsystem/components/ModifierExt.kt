package dev.iurysouza.livematch.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

fun Modifier.roundedClip() = this.clip(RoundedCornerShape(10.dp))
fun Modifier.gradientBackground(
  colors: List<Color>? = null,
  orientation: GradientOrientation = GradientOrientation.Vertical,
): Modifier = composed {
  val colorList = colors ?: listOf(
    MaterialTheme.colors.background,
    MaterialTheme.colors.secondaryVariant,
  )
  background(
    brush = when (orientation) {
      GradientOrientation.Vertical -> Brush.verticalGradient(colorList)
      GradientOrientation.Horizontal -> Brush.horizontalGradient(colorList)
    },
  )
}

fun Modifier.verticalGradient(
  colors: List<Color>? = null,
): Modifier = gradientBackground(colors, GradientOrientation.Vertical)

fun Modifier.horizontalGradient(
  colors: List<Color>? = null,
): Modifier = gradientBackground(colors, GradientOrientation.Horizontal)

sealed class GradientOrientation {
  object Vertical : GradientOrientation()
  object Horizontal : GradientOrientation()
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.thenIf(
  condition: Boolean,
  applyModifier: @Composable Modifier.() -> Modifier,
): Modifier = composed {
  if (condition) {
    this.applyModifier()
  } else {
    this
  }
}

fun Modifier.liveMatchPlaceHolder(duration: Int = 1700): Modifier = composed {
  this
  .clip(RoundedCornerShape(8.dp))
    .placeholder(
      visible = true,
      color = MaterialTheme.colors.onSurface,
      highlight = PlaceholderHighlight.shimmer(
        highlightColor = Color.White.copy(alpha = 0.5f),
        progressForMaxAlpha = 0.5f,
        animationSpec = infiniteRepeatable(
          animation = tween(durationMillis = duration, delayMillis = 200),
          repeatMode = RepeatMode.Restart,
        ),
      ),
    )
}
