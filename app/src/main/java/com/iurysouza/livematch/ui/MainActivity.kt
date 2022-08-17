package com.iurysouza.livematch.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.iurysouza.livematch.ui.navigation.AppNavigation
import com.iurysouza.livematch.ui.theme.livematchTheme
import com.iurysouza.livematch.util.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var jsonParser: JsonParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            livematchTheme {
                AppNavigation(
                    navController = rememberAnimatedNavController(),
                    jsonParser,
                )
            }
        }
    }
}
