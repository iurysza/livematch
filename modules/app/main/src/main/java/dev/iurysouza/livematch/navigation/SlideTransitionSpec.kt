package dev.iurysouza.livematch.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.with
import dev.iurysouza.livematch.common.navigation.Destination
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavTransitionScope

internal val SlideTransitionSpec: NavTransitionScope.(
  action: NavAction,
  from: Destination,
  to: Destination,
) -> ContentTransform =
  { action, _, _ ->
    val direction = if (action == NavAction.Pop) {
      AnimatedContentTransitionScope.SlideDirection.End
    } else {
      AnimatedContentTransitionScope.SlideDirection.Start
    }
    slideIntoContainer(direction) with slideOutOfContainer(direction)
  }
