package dev.iurysouza.livematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.designsystem.theme.LivematchTheme
import dev.iurysouza.livematch.navigation.NavHostScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var jsonParser: JsonParser

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LivematchTheme { NavHostScreen(jsonParser) }
    }
  }
}
