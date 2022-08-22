package dev.iurysouza.livematch.auth

import android.content.SharedPreferences
import arrow.core.Either
import dev.iurysouza.livematch.data.repo.DomainError
import dev.iurysouza.livematch.data.repo.FailedToSave
import dev.iurysouza.livematch.data.repo.KeyNotFound

class AndroidLocalStorage(private val sharedPreferences: SharedPreferences) : KeyValueStorage {
    override fun get(key: String): Either<DomainError, String> =
        Either.catch {
            sharedPreferences.getString(key, null)!!
        }.mapLeft {
            KeyNotFound
        }

    override fun save(key: String, value: String): Either<DomainError, Unit> =
        Either.catch {
            sharedPreferences.edit().putString(key, value).apply()
        }.mapLeft { FailedToSave }
}

interface KeyValueStorage {
    fun get(key: String): Either<DomainError, String>
    fun save(key: String, value: String): Either<DomainError, Unit>
}
