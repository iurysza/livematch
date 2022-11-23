@file:OptIn(ExperimentalAnimationApi::class)

package dev.iurysouza.livematch.ui.navigation

import android.net.Uri
import android.os.Parcelable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import dev.iurysouza.livematch.ui.features.matchlist.Team
import dev.iurysouza.livematch.ui.features.matchthread.Competition
import dev.iurysouza.livematch.ui.features.matchthread.MatchCommentsState
import dev.iurysouza.livematch.ui.features.matchthread.MatchDescriptionState
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.ui.features.matchthread.MatchThreadF
import dev.iurysouza.livematch.ui.features.matchthread.MatchThreadScreen
import dev.iurysouza.livematch.ui.features.matchthread.MediaItem
import dev.iurysouza.livematch.ui.theme.AppBackgroundColor
import dev.iurysouza.livematch.util.JsonParser
import kotlinx.coroutines.NonDisposableHandle.parent
import timber.log.Timber

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    jsonParser: JsonParser,
) {
    AnimatedNavHost(
        modifier =Modifier.background(AppBackgroundColor),
        navController = navController,
        startDestination = Screen.MatchList.name,
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
    val route = Screen.MatchList

    navigation(
        route = route.name,
        startDestination = parent.name,
    ) {
        composable(route = parent.name) {
            MatchListScreen(
                onOpenMatchThread = navController.navigateToRoute(jsonParser) { params ->
                    Screen.MatchThread.createRoute(
                        origin = route,
                        content = params
                    )
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

