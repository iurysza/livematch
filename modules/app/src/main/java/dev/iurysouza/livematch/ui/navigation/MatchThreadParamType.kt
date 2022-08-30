package dev.iurysouza.livematch.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import arrow.core.Either
import dev.iurysouza.livematch.ui.features.matchthread.MatchThread
import dev.iurysouza.livematch.util.JsonParser
import dev.iurysouza.livematch.util.fromJson

class MatchThreadParamType(
    private val jsonParser: JsonParser,
) : NavType<MatchThread>(isNullableAllowed = false) {

    override fun get(
        bundle: Bundle,
        key: String,
    ): MatchThread? = bundle.getParcelable(key)

    override fun parseValue(value: String): MatchThread =
        when (val result = jsonParser.fromJson<MatchThread>(value)) {
            is Either.Left -> throw IllegalArgumentException("Invalid json: $value")
            is Either.Right -> result.value
        }

    override fun put(bundle: Bundle, key: String, value: MatchThread) {
        bundle.putParcelable(key, value)
    }
}