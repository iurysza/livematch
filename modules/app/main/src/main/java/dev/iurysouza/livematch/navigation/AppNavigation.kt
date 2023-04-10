package dev.iurysouza.livematch.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dev.iurysouza.livematch.common.navigation.Destination
import dev.iurysouza.livematch.matchday.MatchDayRoute
import dev.iurysouza.livematch.matchthread.MatchThreadRoute
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun NavHostScreen() {
  val navController = rememberNavController<Destination>(
    startDestination = Destination.MatchDay,
  )
  NavBackHandler(navController)

  AnimatedNavHost(
    controller = navController,
    transitionSpec = { action, _, _ ->
      val direction = if (action == NavAction.Pop) {
        AnimatedContentScope.SlideDirection.End
      } else {
        AnimatedContentScope.SlideDirection.Start
      }
      slideIntoContainer(direction) with slideOutOfContainer(direction)
    },
  ) { screen ->
    when (screen) {
      is Destination.MatchDay -> {
        MatchDayRoute(
          onNavigateMatchThread = { navController.navigate(it) },
        )
      }
      is Destination.MatchThread -> MatchThreadRoute(
        args = screen.matchThread,
        onNavigateUp = { navController.pop() },
      )
      else -> Text("Error")
    }
  }
}
