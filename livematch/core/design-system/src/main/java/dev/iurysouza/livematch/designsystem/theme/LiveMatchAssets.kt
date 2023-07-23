@file:Suppress("MatchingDeclarationName")

package dev.iurysouza.livematch.designsystem.theme

import androidx.compose.ui.graphics.vector.ImageVector
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Booking
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Clock
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Goal
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Kickoff
import dev.iurysouza.livematch.designsystem.theme.livematchassets.Sub
import kotlin.collections.List as ____KtList

public object LiveMatchAssets

private var __AllAssets: ____KtList<ImageVector>? = null

public val LiveMatchAssets.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets = listOf(Sub, Goal, Booking, Clock, Kickoff)
    return __AllAssets!!
  }
