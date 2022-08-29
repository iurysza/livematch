package dev.iurysouza.livematch.ui.navigation

sealed class Screen(val route: String) {

    object MatchThread : Screen(ROUTE_MATCH_THREAD) {
        fun createRoute(root: Screen, matchContent: String): String {
            return "${root.route}/$ROUTE_MATCH_THREAD/$matchContent"
        }
    }

    object MatchList : Screen(ROOT_ROUTE)
}

sealed class LeafScreen(private val route: String) {
    fun defineRoute(parent: Screen) = "${parent.route}/$route"

    object MatchThread : LeafScreen("${ROUTE_MATCH_THREAD}/{$PARAM}")
}


private const val ROUTE_MATCH_THREAD = "matchThread"
private const val ROOT_ROUTE = "matchList"
