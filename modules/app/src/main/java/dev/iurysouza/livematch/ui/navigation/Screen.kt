package dev.iurysouza.livematch.ui.navigation

sealed class Screen(val name: String) {

    object MatchThread : Screen(ROUTE_MATCH_THREAD) {
        fun createRoute(origin: Screen, content: String): String {
            return "${origin.name}/$ROUTE_MATCH_THREAD/$content"
        }
    }

    object Root : Screen("ROOT")
    object MatchList : Screen(ROUTE_MATCH_LIST)
}

sealed class LeafScreen(private val route: String) {
    /**
     * A route is defined by the parent route with leaf route
     */
    fun defineRoute(parent: Screen) = "${parent.name}/$route"

    data class MatchThread(
        val argument: String = "MatchThread",
    ) : LeafScreen("${ROUTE_MATCH_THREAD}/{$argument}")

}

private const val ROUTE_MATCH_THREAD = "matchThread"
private const val ROUTE_MATCH_LIST = "matchList"
