package dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper

import arrow.core.Either
import dev.iurysouza.livematch.webviewtonativeplayer.BuildConfig
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

internal object OkHttpCoroutineClient : HttpClient {

  private const val isVerboseLogging: Boolean = false
  override suspend fun loadUrl(url: String): Either<Exception, String> = Either.catch {
    return@catch withContext(Dispatchers.IO) {
      Timber.v("Loading page content: $url")
      val call = okHttpClient(isVerboseLogging).newCall(Request.Builder().url(url).build())
      val response = suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation { call.cancel() }

        call.enqueue(
          object : Callback {
            override fun onFailure(call: Call, e: IOException) {
              if (!continuation.isCancelled) {
                continuation.resumeWithException(e)
              }
            }

            override fun onResponse(call: Call, response: Response) {
              if (response.isSuccessful) {
                continuation.resume(response)
              } else {
                continuation.resumeWithException(IOException("Unexpected code $response"))
              }
            }
          },
        )
      }
      response.body!!.string()
    }
  }.mapLeft { e -> Exception("Failed to load url", e) }


  private val okHttpClient: (Boolean) -> OkHttpClient by lazy {
    { isVerboseLogging: Boolean ->
      OkHttpClient.Builder()
        .addInterceptor(
          Interceptor { chain ->
            val originalRequest = chain.request()
            val requestWithUserAgent: Request = originalRequest.newBuilder()
              .header("User-Agent", ScrapperHelper.userAgent)
              .build()
            chain.proceed(requestWithUserAgent)
          },
        )
        .addInterceptor(
          HttpLoggingInterceptor().apply {
            level = if (isVerboseLogging && BuildConfig.DEBUG) {
              HttpLoggingInterceptor.Level.BODY
            } else {
              HttpLoggingInterceptor.Level.HEADERS
            }
          },
        ).build()

    }

  }
}

internal interface HttpClient {
  suspend fun loadUrl(url: String): Either<Exception, String>
}
