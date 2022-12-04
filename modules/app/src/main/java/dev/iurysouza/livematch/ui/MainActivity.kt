package dev.iurysouza.livematch.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.iurysouza.livematch.ui.navigation.AppNavigation
import dev.iurysouza.livematch.ui.theme.LivematchTheme
import dev.iurysouza.livematch.core.JsonParser
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var jsonParser: JsonParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LivematchTheme {
                AppNavigation(
                    navController = rememberAnimatedNavController(),
                    jsonParser,
                )
            }
        }
    }
}
