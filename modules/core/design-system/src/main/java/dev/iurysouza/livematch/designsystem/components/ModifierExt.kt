package dev.iurysouza.livematch.designsystem.components

import android.annotation.SuppressLint
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
