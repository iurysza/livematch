package dev.iurysouza.livematch.fakes

import dev.iurysouza.livematch.domain.adapters.KeyValueStorage


class InMemoryKeyValueStorage(
    private val map: MutableMap<String, Any?> = mutableMapOf(),
) : KeyValueStorage {

    override fun getLong(key: String): Long? = map[key] as Long?
    override fun put(key: String, value: Long) {
        map[key] = value
    }

    override fun getString(key: String): String? = map[key] as String?
    override fun put(key: String, value: String) {
        map[key] = value
    }
}
