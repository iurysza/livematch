package dev.iurysouza.livematch.data.storage

import android.app.Activity
import android.content.Context

interface KeyValueStorage {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
}

class AndroidSimpleStorage(
    activity: Activity,
) : KeyValueStorage {

    private val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

    override fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    override fun putString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }
}
