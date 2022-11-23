package dev.iurysouza.livematch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.halilibo.richtext.ui.RichTextThemeIntegration

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

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
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        RichTextThemeIntegration(
            textStyle = { TextStyle(color = TextColor) },
            ProvideTextStyle = { newTextStyle, content ->
                CompositionLocalProvider(compositionLocalOf { TextStyle(color = TextColor) } provides newTextStyle) {
                    content()
                }
            },
            ProvideContentColor = { newColor, content ->
                CompositionLocalProvider(compositionLocalOf { TextColor } provides newColor) {
                    content()
                }
            },
            content = content
        )

    }
}
