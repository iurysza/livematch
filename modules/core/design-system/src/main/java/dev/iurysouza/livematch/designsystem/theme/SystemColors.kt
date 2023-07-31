package dev.iurysouza.livematch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemColors(
  systemBarColor: Color? = null,
  navigationBarColor: Color? = null,
) {
  val isDarkTheme = isSystemInDarkTheme()
  val systemUiController = rememberSystemUiController()
  SideEffect {
    systemBarColor?.let { systemUiController.setSystemBarsColor(it, darkIcons = !isDarkTheme) }
    navigationBarColor?.let { systemUiController.setNavigationBarColor(it, darkIcons = !isDarkTheme) }
  }
}
