package dev.iurysouza.livematch.fakes

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.data.RedditApi
import java.util.Base64
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object FakeRedditApi {
    fun createWith(mockWebServer: MockWebServer): RedditApi {
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


        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(factory)
            .client(okhttpClient)
            .build().create(RedditApi::class.java)
    }

}
