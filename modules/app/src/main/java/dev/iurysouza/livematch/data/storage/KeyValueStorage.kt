package dev.iurysouza.livematch.data.storage

import android.content.SharedPreferences
import javax.inject.Inject

interface KeyValueStorage {
    fun getLong(key: String): Long?
    fun getString(key: String): String?
    fun put(key: String, value: Long)
    fun put(key: String, value: String)
}

class SystemStorage @Inject constructor(
    private val sharedPref: SharedPreferences,
) : KeyValueStorage {

    override fun getString(key: String): String? = sharedPref.getString(key, null)

    override fun getLong(key: String): Long = sharedPref.getLong(key, 0)

    override fun put(key: String, value: Long) = sharedPref.update { putLong(key, value) }

    override fun put(key: String, value: String) = sharedPref.update { putString(key, value) }

    private fun SharedPreferences.update(
        block: SharedPreferences.Editor.() -> Unit,
    ) = edit().apply(block).apply()
}
