package dev.iurysouza.livematch.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import arrow.core.Either
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.common.fromJson
import dev.iurysouza.livematch.matchthread.MatchThreadRoute
import dev.iurysouza.livematch.matchthread.models.MatchThread


fun NavGraphBuilder.addMatchThreadNavGraph(
  navController: NavController,
  jsonParser: JsonParser,
  parent: Screen,
) {
  navigation(
    route = Screen.MatchThread.name,
    startDestination = parent.name,
  ) {
    val matchThreadScreen = LeafScreen.MatchThread()
    composable(
      route = matchThreadScreen.defineRoute(parent),
      arguments = listOf(
        navArgument(matchThreadScreen.argument) {
          type = MatchThreadParamType(jsonParser)
        },
      ),
    ) { backStackEntry ->
      MatchThreadRoute(
        navigateUp = navController::navigateUp,
        matchThread = backStackEntry.getParcelable(matchThreadScreen.argument),
      )
    }
  }
}

class MatchThreadParamType(
  private val jsonParser: JsonParser,
) : NavType<MatchThread>(isNullableAllowed = false) {

  @Suppress("DEPRECATION")
  override fun get(
    bundle: Bundle,
    key: String,
  ): MatchThread? = bundle.getParcelable(key)

  override fun parseValue(value: String): MatchThread =
    when (val result = jsonParser.fromJson<MatchThread>(value)) {
      is Either.Left -> throw IllegalArgumentException("Invalid json: $value")
      is Either.Right -> result.value
    }

  override fun put(bundle: Bundle, key: String, value: MatchThread) {
    bundle.putParcelable(key, value)
  }
}
