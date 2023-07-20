package dev.iurysouza.livematch.designsystem.components

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

fun Modifier.roundedClip() = this.clip(RoundedCornerShape(10.dp))
fun Modifier.gradientBackground(
  colors: List<Color>? = null,
): Modifier = composed {
  background(
    brush = Brush.verticalGradient(
      colors ?: listOf(
        MaterialTheme.colors.background,
        MaterialTheme.colors.secondaryVariant,
      ),
    ),
  )
}

@Composable
fun Modifier.thenIf(
  scrollable: Boolean,
  applyModifier: @Composable Modifier.() -> Modifier,
): Modifier =
  if (scrollable) {
    this.applyModifier()
  } else {
    this
  }
