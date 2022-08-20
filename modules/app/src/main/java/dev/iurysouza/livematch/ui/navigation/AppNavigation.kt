@file:OptIn(ExperimentalAnimationApi::class)

package dev.iurysouza.livematch.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import dev.iurysouza.livematch.ui.features.postDetail.PostDetailScreen
import dev.iurysouza.livematch.ui.features.posts.PostsScreen
import dev.iurysouza.livematch.util.JsonParser


@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    jsonParser: JsonParser,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Posts.route,
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
    ) {
        addPostsTopLevel(navController, jsonParser)
        addPostsDetail(navController = navController, jsonParser, parent = Screen.Posts)
    }
}

private fun NavGraphBuilder.addPostsTopLevel(
    navController: NavController,
    jsonParser: JsonParser,
) {
    val startDestination = "root"
    navigation(
        startDestination = startDestination,
        route = Screen.Posts.route,
    ) {
        composable(route = startDestination) {
            PostsScreen(openPostDetail = { post ->
                navController.navigate(
                    Screen.PostDetail.createRoute(Screen.Posts, jsonParser.toJson(post))
                )
            })
        }
    }
}

private fun NavGraphBuilder.addPostsDetail(
    navController: NavController,
    jsonParser: JsonParser,
    parent: Screen,
) {
    navigation(
        route = Screen.PostDetail.route,
        startDestination = parent.route,
    ) {
        composable(
            route = LeafScreen.PostDetail.defineRoute(parent),
            arguments = listOf(
                navArgument("post") {
                    type = PostParamType(jsonParser)
                },
            ),
        ) {
            PostDetailScreen(
                navigateUp = navController::navigateUp,
                post = it.arguments!!.getParcelable("post")!!,
            )
        }
    }
}
