package dev.iurysouza.livematch.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.iurysouza.livematch.R
import dev.iurysouza.livematch.common.navigation.Destination
import dev.iurysouza.livematch.designsystem.components.ErrorScreen
import dev.iurysouza.livematch.designsystem.theme.SystemColors
import dev.iurysouza.livematch.matchday.MatchDayRoute
import dev.iurysouza.livematch.matchthread.MatchThreadRoute
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun LiveMatchNavHost() {
  val navController = rememberNavController<Destination>(
    startDestination = Destination.MatchDay,
  )
  NavBackHandler(navController)

  AnimatedNavHost(
    controller = navController,
    transitionSpec = SlideTransitionSpec,
  ) { screen ->
    SystemColors(
      systemBarColor = MaterialTheme.colors.background,
      navigationBarColor = MaterialTheme.colors.secondaryVariant,
    )
    when (screen) {
      is Destination.MatchDay -> MatchDayRoute { navController.navigate(it) }
      is Destination.MatchThread -> MatchThreadRoute(
        args = screen.matchThread,
        onNavigateUp = { navController.pop() },
      )
      else -> ErrorScreen(msg = stringResource(R.string.route_not_found))
    }
  }
}
