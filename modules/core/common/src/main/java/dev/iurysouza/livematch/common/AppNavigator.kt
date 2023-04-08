package dev.iurysouza.livematch.common

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import arrow.core.continuations.either

@Suppress("DEPRECATION")
fun <T : Parcelable> NavBackStackEntry.getParcelable(key: String): T =
  requireNotNull(arguments?.getParcelable(key))

fun <T : Any> NavController.navigateToRoute(
  jsonParser: JsonParser,
  routeBuilder: (String) -> String,
): (T) -> Unit = { argument: T ->
  either.eager { Uri.encode(jsonParser.toJson(argument).bind()) }
    .fold(
      { timber.log.Timber.e(it.toString()) },
      { jsonObject ->
        navigate(routeBuilder(jsonObject))
      },
    )
}


interface NavigationBuilder {
  fun <T : Parcelable> navigateTo(param: T, routeBuilder: (String) -> String)
}
