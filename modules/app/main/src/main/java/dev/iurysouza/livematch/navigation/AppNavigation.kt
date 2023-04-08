package dev.iurysouza.livematch.navigation

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.matchday.addMatchDayNavGraph
import dev.iurysouza.livematch.matchthread.addMatchThreadNavGraph

@Composable
internal fun AppNavigation(
  navController: NavHostController,
  jsonParser: JsonParser,
) {
  AnimatedNavHost(
    modifier = Modifier.background(MaterialTheme.colors.background),
    navController = navController,
    startDestination = "matchDay",
    enterTransition = { defaultEnterTransition(initialState, targetState) },
    exitTransition = { defaultExitTransition(initialState, targetState) },
  ) {
    addMatchDayNavGraph(navController, jsonParser)
    addMatchThreadNavGraph(navController, jsonParser)
  }
}
