package dev.iurysouza.livematch.matchday

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.common.navigateToRoute

private const val ROUTE_ROOT = "root"
private const val ROUTE_MATCH_THREAD = "matchThread"
private const val ROUTE_MATCH_DAY = "matchDay"
fun NavGraphBuilder.addMatchDayNavGraph(
  navController: NavController,
  jsonParser: JsonParser,
) {
  composable(route = ROUTE_MATCH_DAY) {
    MatchDayRoute(
      onOpenMatchThread = navController.navigateToRoute(jsonParser) { params ->
        "$ROUTE_MATCH_THREAD/$params"
      },
    )
  }
}
