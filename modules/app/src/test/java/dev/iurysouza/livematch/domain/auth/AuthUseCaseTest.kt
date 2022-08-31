package dev.iurysouza.livematch.domain.auth

import dev.iurysouza.livematch.domain.adapters.NetworkError
import dev.iurysouza.livematch.fakes.InMemoryKeyValueStorage
import dev.iurysouza.livematch.fakes.StubNetworkDatasource
import dev.iurysouza.livematch.fakes.anAccessTokenEntity
import dev.iurysouza.livematch.util.secondsAgo
import dev.iurysouza.livematch.util.secondsFromNow
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldNotBe

class AuthUseCaseTest : BehaviorSpec({

    given("there's no token in the local storage") {
        val localStorage = mutableMapOf<String, Any?>()
        val accessToken = "token"
        val sut = RefreshTokenIfNeededUseCase(
            networkDataSource = StubNetworkDatasource(
                returnAccessToken = anAccessTokenEntity(accessToken)
            ),
            storage = AuthStorage(InMemoryKeyValueStorage(localStorage))
        )
        `when`("the user tries to refresh the token") {
            sut()
            then("the local storage should be updated with the token fetched from the network") {
                localStorage shouldContain ("accessToken" to accessToken)
            }
        }
    }
    given("that the token in local storage is expired") {
        val expiredToken = "expiredToken"
        val localStorage = mutableMapOf<String, Any?>(
            "accessToken" to expiredToken,
            "expirationDate" to 60.secondsAgo()
        )
        val sut = RefreshTokenIfNeededUseCase(
            networkDataSource = StubNetworkDatasource(),
            storage = AuthStorage(InMemoryKeyValueStorage(localStorage))
        )

        `when`("the user tries to refresh the token") {
            sut()
            then("the local storage token should be updated") {
                localStorage["accessToken"] shouldNotBe expiredToken
            }
        }
    }

    given("that the token in local storage is valid") {
        val accessToken = "token"
        val localStorage = mutableMapOf<String, Any?>(
            "accessToken" to accessToken,
            "expirationDate" to 60.secondsFromNow()
        )
        val sut = RefreshTokenIfNeededUseCase(
            networkDataSource = StubNetworkDatasource(),
            storage = AuthStorage(InMemoryKeyValueStorage(localStorage))
        )

        `when`("the user tries to refresh the token") {
            sut()
            then("the token in storage should be the same") {
                localStorage shouldContain Pair("accessToken", accessToken)
            }
        }
    }
    given("that there is no token in the storage") {
        and("we can't reach the network") {
            val networkDataSource = StubNetworkDatasource(accessTokenError = Throwable())
            `when`("the user tries to refresh the token") {
                val sut = RefreshTokenIfNeededUseCase(
                    networkDataSource = networkDataSource,
                    storage = AuthStorage(InMemoryKeyValueStorage())
                )
                val result = sut()
                then("the result should be a NetworkError") {
                    result shouldBeLeft NetworkError()
                }
            }
        }
    }
})


