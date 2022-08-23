package dev.iurysouza.livematch.domain.auth

import dev.iurysouza.livematch.data.Fixtures.authJsonResponse
import dev.iurysouza.livematch.data.network.RedditApi
import dev.iurysouza.livematch.fakes.FakeRedditApi
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
//            val sut = AuthUseCase(
//                redditApi = redditApi!!,
//                storage = AuthStorage(InMemoryKeyValueStorage())
//            )

//            val result = sut.refreshTokenIfNeeded()
            then("should return a success response") {
//                result shouldBeRight Unit
            }
        }
    }

    afterSpec {
        mockWebServer.shutdown()
    }
})


