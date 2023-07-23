package dev.iurysouza.livematch.common

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

interface ResourceProvider {
  fun getString(@StringRes id: Int): String
}

class SystemResourceProvider @Inject constructor(
  private val context: Context,
) : ResourceProvider {
  override fun getString(@StringRes id: Int): String {
    return context.getString(id)
  }
}
