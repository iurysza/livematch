package dev.iurysouza.livematch.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.common.remap
import dev.iurysouza.livematch.matchday.MatchDayRoute
import dev.iurysouza.livematch.matchday.models.MatchThread
import dev.iurysouza.livematch.matchthread.MatchThreadRoute
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import dev.iurysouza.livematch.matchthread.models.MatchThread as TargetMatchThread

@Composable
fun NavHostScreen(
  jsonParser: JsonParser,
) {
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
          onOpenMatchThread = { sourceMatchThread ->
            jsonParser.remap<MatchThread, TargetMatchThread>(sourceMatchThread).fold(
              ifLeft = { navController.navigate(Destination.Error) },
              ifRight = { navController.navigate(Destination.MatchThread(it)) },
            )
          },
        )
      }
      is Destination.MatchThread -> MatchThreadRoute(
        matchThread = screen.matchThread,
        navigateUp = { navController.pop() },
      )
      Destination.Error -> Text("Error")
    }
  }
}

