package dev.iurysouza.livematch.fakes

import dev.iurysouza.livematch.data.storage.KeyValueStorage

class InMemoryKeyValueStorage(
    private val map: MutableMap<String, String> = mutableMapOf(),
) : KeyValueStorage {
    override fun getString(key: String): String? = map[key]
    override fun putString(key: String, value: String) {
        map[key] = value
    }
}
