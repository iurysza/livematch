package dev.iurysouza.livematch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.halilibo.richtext.ui.RichTextThemeIntegration


private val DarkColorPalette = darkColorScheme(
  primary = AppAccent1Dark,
  secondary = AppAccent2Dark,
//  primaryVariant = AppAccent2Dark,
  background = AppBackgroundDark,
  onPrimary = AppText1Dark,
  onBackground = AppText2Dark,
  onSurface = AppText3Dark,
  tertiary = AppSecondaryDark,
//  secondaryVariant = AppSecondaryDark,
)

private val LightColorPalette = lightColorScheme(
  primary = AppAccent1Light,
  secondary = AppAccent2Light,
//  primaryVariant = AppAccent2Light,
  background = AppBackgroundLight,
  onPrimary = AppText1Light,
  onBackground = AppText2Light,
  onSurface = AppText3Light,
  tertiary = AppSecondaryLight,
//  secondaryVariant = AppSecondaryLight,
)

@Composable
fun LiveMatchThemePreview(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit,
) {
  LivematchTheme(darkTheme = darkTheme, content = content)
}

@Composable
fun LivematchTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
    colorScheme = colors,
    typography = Typography(),
//    shapes = Shapes,
  ) {
    val textColor = MaterialTheme.colorScheme.onBackground

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
