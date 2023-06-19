package dev.iurysouza.livematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.navigation.LiveMatchNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LivematchTheme { LiveMatchNavHost() }
    }
  }
}
