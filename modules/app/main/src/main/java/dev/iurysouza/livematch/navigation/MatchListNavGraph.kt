package dev.iurysouza.livematch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.matchday.MatchLisRoute

fun NavGraphBuilder.addMatchListNavGraph(
  navController: NavController,
  jsonParser: JsonParser,
  parent: Screen,
) {
  val route = Screen.MatchList

  navigation(
    route = route.name,
    startDestination = parent.name,
  ) {
    composable(route = parent.name) {
      MatchLisRoute(
        onOpenMatchThread = navController.navigateToRoute(jsonParser) { params ->
          Screen.MatchThread.createRoute(
            origin = route,
            content = params,
          )
        },
      )
    }
  }
}
