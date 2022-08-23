package dev.iurysouza.livematch.domain.auth

import arrow.core.Either
import dev.iurysouza.livematch.domain.DomainError
import dev.iurysouza.livematch.fakes.InMemoryKeyValueStorage
import dev.iurysouza.livematch.fakes.StubNetworkDatasource
import dev.iurysouza.livematch.fakes.fakeAccessTokenEntity
import dev.iurysouza.livematch.util.secondsAgo
import dev.iurysouza.livematch.util.secondsFromNow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldNotBe

class AuthUseCaseTest : BehaviorSpec({

    given("there's no token in the local storage") {
        val localStorage = mutableMapOf<String, Any?>()
        val sut = AuthUseCase(
            networkDataSource = StubNetworkDatasource(returnAccessToken = fakeAccessTokenEntity),
            storage = AuthStorage(InMemoryKeyValueStorage(localStorage))
        )
        `when`("the user tries to refresh the token") {
            sut.refreshTokenIfNeeded()
            then("the local storage should be updated with the token fetched from the network") {
                localStorage shouldContain Pair("accessToken", fakeAccessTokenEntity.accessToken)
            }
        }
    }
    given("that the token in local storage is expired") {
        val expiredToken = "expiredToken"
        val localStorage = mutableMapOf<String, Any?>(
            "accessToken" to expiredToken,
            "expirationDate" to 60.secondsAgo()
        )
        val sut = AuthUseCase(
            networkDataSource = StubNetworkDatasource(),
            storage = AuthStorage(InMemoryKeyValueStorage(localStorage))
        )

        `when`("the user tries to refresh the token") {
            val res = sut.refreshTokenIfNeeded()
            when (res) {
                is Either.Left -> {
                    val value: DomainError = res.value
                    println("====================error: $value")
                }
                is Either.Right -> println("====================success ")
            }
            then("the local storage token should be updated") {
                localStorage["accessToken"] shouldNotBe expiredToken
            }
        }
    }

    given("that the token in local storage is valid") {
        val localStorage = mutableMapOf<String, Any?>(
            "accessToken" to fakeAccessTokenEntity.accessToken,
            "expirationDate" to 60.secondsFromNow()
        )
        val sut = AuthUseCase(
            networkDataSource = StubNetworkDatasource(),
            storage = AuthStorage(InMemoryKeyValueStorage(localStorage))
        )

        `when`("the user tries to refresh the token") {
            sut.refreshTokenIfNeeded()
            then("the token in storage should be the same") {
                localStorage shouldContain Pair("accessToken", fakeAccessTokenEntity.accessToken)
            }
        }
    }
})


