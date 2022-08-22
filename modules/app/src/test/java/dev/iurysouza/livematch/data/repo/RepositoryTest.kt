package dev.iurysouza.livematch.data.repo

import arrow.core.right
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.auth.AuthStorage
import dev.iurysouza.livematch.auth.AuthUseCase
import dev.iurysouza.livematch.auth.SimpleKeyValueStorage
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.util.CoroutineTestRule
import io.kotest.matchers.shouldBe
import java.util.Base64
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
class RepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()


    lateinit var redditApi: RedditApi
    lateinit var mockWebServer: MockWebServer

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val username = BuildConfig.CLIENT_ID
        val password = ""
        val credentials = "Basic ${
            Base64.getEncoder().encodeToString(
                "$username:$password".toByteArray()
            )
        }"
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", credentials).build()
                )
            }.build()

        val factory = MoshiConverterFactory.create(Moshi.Builder()
            .apply { add(KotlinJsonAdapterFactory()) }
            .build())

        redditApi = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(factory)
            .client(okhttpClient)
            .build().create(RedditApi::class.java)
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """
                    {
                        "access_tjorge": "access_token",
                        "token_type": "bearer",
                        "expires_in": 3600,
                        "scope": "identity"
                    }
                    """.trimIndent()
            )
        )
    }


    @Test
    fun `When request to users api returns a valid object then repository should emit success state`() = runTest {
//            val sut = AuthUseCase(
//                redditApi,
//                AuthStorage(InMemoryKeyValueStorage(mutableMapOf()))
//            )
//            val result = sut.validateAccessToken()
//            result shouldBe right()
        }
}

//private class InMemoryKeyValueStorage(private val map: MutableMap<String, String>) :
//    SimpleKeyValueStorage {
//    override fun getString(key: String): String? = map[key]
//    override fun putString(key: String, value: String) {
//        map[key] = value
//    }
//}
