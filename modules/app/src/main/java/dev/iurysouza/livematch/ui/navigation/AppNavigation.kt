@file:OptIn(ExperimentalAnimationApi::class)

package dev.iurysouza.livematch.ui.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
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
        addMatchListTopLevel(navController, jsonParser)
        addMatchThread(navController, jsonParser, Screen.MatchList)
    }
}

private fun NavGraphBuilder.addMatchListTopLevel(
    navController: NavController,
    jsonParser: JsonParser,
) {
    val startDestination = "root"
    navigation(
        startDestination = startDestination,
        route = Screen.MatchList.route,
    ) {
        composable(route = startDestination) {
            MatchListScreen(
                onOpenMatchThread = { matchThread ->
                    either.eager { jsonParser.toJson(matchThread).bind() }
                        .fold(
                            { Log.e("LiveMatch", "$it") },
                            { matchContent ->
                                navController.navigate(
                                    Screen.MatchThread.createRoute(Screen.MatchList, matchContent))
                            }
                        )
                })
        }
    }
}

private fun NavGraphBuilder.addMatchThread(
    navController: NavController,
    jsonParser: JsonParser,
    parent: Screen,
) {
    navigation(
        route = Screen.MatchThread.route,
        startDestination = parent.route,
    ) {
        composable(
            route = LeafScreen.MatchThread.defineRoute(parent),
            arguments = listOf(
                navArgument(MATCH_THREAD_ARGUMENT) {
                    type = MatchThreadParamType(jsonParser)
                },
            ),
        ) {
            MatchThreadScreen(
                navigateUp = navController::navigateUp,
                matchThread = it.arguments!!.getParcelable(MATCH_THREAD_ARGUMENT)!!,
            )
        }
    }
}

const val MATCH_THREAD_ARGUMENT = "matchThread"
