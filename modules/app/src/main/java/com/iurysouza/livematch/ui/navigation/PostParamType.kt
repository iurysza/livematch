package com.iurysouza.livematch.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.iurysouza.livematch.ui.features.posts.Post
import com.iurysouza.livematch.util.JsonParser
import com.iurysouza.livematch.util.fromJson

class PostParamType(private val jsonParser: JsonParser) : NavType<Post>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): Post? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Post {
        return jsonParser.fromJson<Post>(value)!!
    }

    override fun put(bundle: Bundle, key: String, value: Post) {
        bundle.putParcelable(key, value)
    }
}
