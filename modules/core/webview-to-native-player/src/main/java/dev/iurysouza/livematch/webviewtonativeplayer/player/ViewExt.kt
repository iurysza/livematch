package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.view.View

fun View.showIf(condition: Boolean) {
  visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.show() = showIf(true)
fun View.hide() = showIf(false)
