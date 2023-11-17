package dev.iurysouza.livematch.common

import timber.log.Timber

val LiveMatchDebugTree: Timber.Tree = object : Timber.DebugTree() {
  override fun createStackElementTag(element: StackTraceElement): String {
    return "LiveMatch ${super.createStackElementTag(element)}"
  }
}
