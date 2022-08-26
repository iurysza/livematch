package dev.iurysouza.livematch.util

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.squareup.moshi.Moshi
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.domain.SerializationError
import javax.inject.Inject

interface JsonParser {
    fun <T : Any> toJson(any: T): Either<DomainError, String>
    fun <T> fromJson(json: String, classOfT: Class<T>): Either<SerializationError, T>
}

class MoshiJsonParser @Inject constructor(private val moshi: Moshi) : JsonParser {
    override fun <T : Any> toJson(any: T): Either<DomainError, String> = catch {
        moshi.adapter(Any::class.java).toJson(any)
    }.mapLeft { SerializationError(it.message) }


    override fun <T> fromJson(json: String, classOfT: Class<T>): Either<SerializationError, T> =
        catch {
            moshi.adapter(classOfT).fromJson(json)!!
        }.mapLeft { SerializationError(it.message) }
}

inline fun <reified T> JsonParser.fromJson(json: String): Either<SerializationError, T> =
    fromJson(json, T::class.java)
