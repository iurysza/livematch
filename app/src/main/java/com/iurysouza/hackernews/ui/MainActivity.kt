package com.iurysouza.hackernews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.iurysouza.hackernews.ui.navigation.AppNavigation
import com.iurysouza.hackernews.ui.theme.HackerNewsTheme
import com.iurysouza.hackernews.util.JsonParser
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
            HackerNewsTheme {
                AppNavigation(
                    navController = rememberAnimatedNavController(),
                    jsonParser,
                )
            }
        }
    }
}
