@file:OptIn(ExperimentalAnimationApi::class)

package dev.iurysouza.livematch.ui.navigation

import android.net.Uri
import android.os.Parcelable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import arrow.core.continuations.either
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import dev.iurysouza.livematch.ui.features.matchlist.MatchListScreen
import dev.iurysouza.livematch.ui.features.matchthread.MatchThreadScreen
import dev.iurysouza.livematch.util.JsonParser
import timber.log.Timber

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    jsonParser: JsonParser,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.MatchList.route,
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
    ) {
        addMatchListNavGraph(navController, jsonParser, Screen.Root)
        addMatchThreadNavGraph(navController, jsonParser, Screen.MatchList)
    }
}

private fun NavGraphBuilder.addMatchListNavGraph(
    navController: NavController,
    jsonParser: JsonParser,
    parent: Screen,
) {
    navigation(
        startDestination = parent.route,
        route = Screen.MatchList.route,
    ) {
        composable(route = parent.route) {
            MatchListScreen(
                onOpenMatchThread = navController.navigateToRoute(jsonParser) { params ->
                    Screen.MatchThread.createRoute(Screen.MatchList, params)
                }
            )
        }
    }
}

private fun NavGraphBuilder.addMatchThreadNavGraph(
    navController: NavController,
    jsonParser: JsonParser,
    parent: Screen,
) {
    navigation(
        route = Screen.MatchThread.route,
        startDestination = parent.route,
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
            MatchThreadScreen(
                navigateUp = navController::navigateUp,
                matchThread = backStackEntry.getParcelable(matchThreadScreen.argument)
            )
        }
    }
}

fun <T : Parcelable> NavBackStackEntry.getParcelable(key: String): T =
    requireNotNull(arguments?.getParcelable(key))

fun <T : Any> NavController.navigateToRoute(
    jsonParser: JsonParser,
    routeBuilder: (String) -> String,
): (T) -> Unit = { argument: T ->
    either.eager { Uri.encode(jsonParser.toJson(argument).bind()) }
        .fold(
            { Timber.e(it.toString()) },
            { jsonObject -> navigate(routeBuilder(jsonObject)) }
        )
}

