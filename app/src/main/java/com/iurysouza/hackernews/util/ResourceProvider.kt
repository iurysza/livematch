package com.iurysouza.hackernews.util

import android.content.Context
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes id: Int): String
}

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}
