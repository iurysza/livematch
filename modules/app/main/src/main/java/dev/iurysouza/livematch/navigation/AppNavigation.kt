package dev.iurysouza.livematch.navigation

import android.net.Uri
import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import arrow.core.continuations.either
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dev.iurysouza.livematch.common.JsonParser
import timber.log.Timber

@Composable
internal fun AppNavigation(
  navController: NavHostController,
  jsonParser: JsonParser,
) {
  AnimatedNavHost(
    modifier = Modifier.background(MaterialTheme.colors.background),
    navController = navController,
    startDestination = Screen.MatchList.name,
    enterTransition = { defaultEnterTransition(initialState, targetState) },
    exitTransition = { defaultExitTransition(initialState, targetState) },
  ) {
    addMatchListNavGraph(
      parent = Screen.Root,
      navController = navController,
      jsonParser = jsonParser,
    )
    addMatchThreadNavGraph(
      parent = Screen.MatchList,
      navController = navController,
      jsonParser = jsonParser,
    )
  }
}


@Suppress("DEPRECATION")
fun <T : Parcelable> NavBackStackEntry.getParcelable(key: String): T =
  requireNotNull(arguments?.getParcelable(key))

fun <T : Any> NavController.navigateToRoute(
  jsonParser: JsonParser,
  routeBuilder: (String) -> String,
): (T) -> Unit = { argument: T ->
  either.eager { Uri.encode(jsonParser.toJson(argument).bind()) }
    .fold(
      { Timber.e(it.toString()) },
      { jsonObject -> navigate(routeBuilder(jsonObject)) },
    )
}
