package dev.iurysouza.livematch.matchthread

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import arrow.core.Either
import dev.iurysouza.livematch.common.JsonParser
import dev.iurysouza.livematch.common.fromJson
import dev.iurysouza.livematch.common.getParcelable
import dev.iurysouza.livematch.matchthread.models.MatchThread

private const val ROUTE_MATCH_DAY = "matchDay"
private const val ROUTE_MATCH_THREAD = "matchThread"

fun NavGraphBuilder.addMatchThreadNavGraph(
  navController: NavController,
  jsonParser: JsonParser,
) {
  composable(
    route = "$ROUTE_MATCH_THREAD/{MatchThread}",
    arguments = listOf(
      navArgument("MatchThread") {
        type = MatchThreadParamType(jsonParser)
      },
    ),
  ) { backStackEntry ->
    MatchThreadRoute(
      navigateUp = navController::navigateUp,
      matchThread = backStackEntry.getParcelable("MatchThread"),
    )
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
