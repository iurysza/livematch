package dev.iurysouza.livematch.util

import android.content.Context
import android.widget.Toast

fun Context.longToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.shortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
