package dev.iurysouza.livematch.ui.navigation

sealed class Screen(val route: String) {

    object PostDetail : Screen("postDetail") {
        fun createRoute(root: Screen, postContent: String): String {
            return "${root.route}/postDetail/$postContent"
        }
    }

    object Posts : Screen("posts")
}

sealed class LeafScreen(private val route: String) {
    fun defineRoute(parent: Screen) = "${parent.route}/$route"

    object PostDetail : LeafScreen("postDetail/{post}")
}

