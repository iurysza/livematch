package com.iurysouza.hackernews.util

import com.squareup.moshi.Moshi
import javax.inject.Inject

interface JsonParser {
  fun toJson(any: Any): String
  fun <T> fromJson(json: String, classOfT: Class<T>): T?
}

class MoshiJsonParser @Inject constructor(private val moshi: Moshi) : JsonParser {
  override fun toJson(any: Any): String {
    return moshi.adapter(Any::class.java).toJson(any)
  }

  override fun <T> fromJson(json: String, classOfT: Class<T>): T? {
    return moshi.adapter(classOfT).fromJson(json)
  }
}

inline fun <reified T> JsonParser.fromJson(json: String): T? = fromJson(json, T::class.java)
