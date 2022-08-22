package dev.iurysouza.livematch.data.repo

import dev.iurysouza.livematch.auth.AuthStorage
import dev.iurysouza.livematch.auth.AuthUseCase
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.data.repo.Fixtures.authJsonResponse
import dev.iurysouza.livematch.data.storage.KeyValueStorage
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer


class MyMockServerTest : BehaviorSpec({
    val mockWebServer = MockWebServer()
    var redditApi: RedditApi? = null

    beforeSpec {
        redditApi = FakeRedditApi.createWith(mockWebServer)
        mockWebServer.start()
    }

    given("that the server is up") {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(authJsonResponse)
        )

        `when`("we check if token is valid") {
            val sut = AuthUseCase(
                redditApi = redditApi!!,
                storage = AuthStorage(InMemoryKeyValueStorage())
            )

            val result = sut.validateAccessToken()
            then("should return a success response") {
                result shouldBeRight Unit
            }
        }
    }

    afterSpec {
        mockWebServer.shutdown()
    }
})


private class InMemoryKeyValueStorage(
    private val map: MutableMap<String, String> = mutableMapOf(),
) : KeyValueStorage {
    override fun getString(key: String): String? = map[key]
    override fun putString(key: String, value: String) {
        map[key] = value
    }
}
