package dev.iurysouza.livematch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.halilibo.richtext.ui.RichTextThemeIntegration

private val DarkColorPalette = darkColors(
  primary = AppAccent1Dark,
  primaryVariant = AppAccent2Dark,
  background = AppBackgroundDark,
  onPrimary = AppText1Dark,
  onBackground = AppText2Dark,
  onSurface = AppText3Dark,
  secondaryVariant = AppSecondaryDark,
)

private val LightColorPalette = lightColors(
  primary = AppAccent1Light,
  primaryVariant = AppAccent2Light,
  background = AppBackgroundLight,
  onPrimary = AppText1Light,
  onBackground = AppText2Light,
  onSurface = AppText3Light,
  secondaryVariant = AppSecondaryLight,
)

@Composable
fun LivematchTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  isPreview: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
    colors = colors,
    typography = Typography(),
    shapes = Shapes,
  ) {
    val textColor = MaterialTheme.colors.onBackground
    val secondary = MaterialTheme.colors.secondaryVariant
    val backgroundColor = MaterialTheme.colors.background

    if (!isPreview) {
      // Fixes issue with Compose Previews breaking
      val systemUiController = rememberSystemUiController()
      SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor, darkIcons = !darkTheme)
        systemUiController.setNavigationBarColor(secondary, darkIcons = !darkTheme)
      }
    }
    RichTextThemeIntegration(
      textStyle = { TextStyle(color = textColor) },
      ProvideTextStyle = { newTextStyle, content ->
        CompositionLocalProvider(compositionLocalOf { TextStyle(color = textColor) } provides newTextStyle) {
          content()
        }
      },
      ProvideContentColor = { newColor, content ->
        CompositionLocalProvider(compositionLocalOf { textColor } provides newColor) {
          content()
        }
      },
      content = content,
    )
  }
}
