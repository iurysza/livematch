package dev.iurysouza.livematch.domain.auth

import dev.iurysouza.livematch.domain.Fixtures.commentsResponse
import dev.iurysouza.livematch.fakes.FakeRedditApi
import dev.iurysouza.livematch.reddit.data.RedditApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

class AuthRequestTest : BehaviorSpec({
  val mockWebServer = MockWebServer()
  var redditApi: RedditApi? = null

  mockWebServer.start()

  beforeSpec {
    redditApi = FakeRedditApi.createWith(mockWebServer)
  }

  given("That the server is running") {
    mockWebServer.enqueue(
      MockResponse().setResponseCode(200).setBody(commentsResponse),
    )

    `when`("fetchComments is called") {
      val result = redditApi!!.fetchComments("any")
      then("the result should not be null") {
        result shouldNotBe null
      }
    }
  }

  afterSpec {
    mockWebServer.shutdown()
  }
},)
