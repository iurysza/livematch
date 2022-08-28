package dev.iurysouza.livematch.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import arrow.core.Either
import dev.iurysouza.livematch.ui.features.matchlist.Post
import dev.iurysouza.livematch.util.JsonParser
import dev.iurysouza.livematch.util.fromJson

class PostParamType(private val jsonParser: JsonParser) : NavType<Post>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): Post? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Post {
        return when (val result = jsonParser.fromJson<Post>(value)) {
            is Either.Left -> {
                return Post(body = "", id = "", title = "", userId = 0, bgColor = 0)
            }
            is Either.Right -> result.value
        }
    }

    override fun put(bundle: Bundle, key: String, value: Post) {
        bundle.putParcelable(key, value)
    }
}
