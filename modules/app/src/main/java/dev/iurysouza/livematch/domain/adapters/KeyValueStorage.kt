package dev.iurysouza.livematch.domain.adapters

interface KeyValueStorage {
    fun getLong(key: String): Long?
    fun getString(key: String): String?
    fun put(key: String, value: Long)
    fun put(key: String, value: String)
}
