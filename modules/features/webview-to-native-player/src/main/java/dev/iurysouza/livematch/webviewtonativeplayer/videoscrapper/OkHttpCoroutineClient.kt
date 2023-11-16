package dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper

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
  override suspend fun loadUrl(url: String): String? = withContext(Dispatchers.IO) {
    try {
      val call = okHttpClient(isVerboseLogging).newCall(Request.Builder().url(url).build())
      val response = suspendCancellableCoroutine<Response> { continuation ->
        continuation.invokeOnCancellation {
          call.cancel()
        }

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
      return@withContext response.body?.string()
    } catch (ex: Exception) {
      Timber.e(ex, "Failed to run request")
      null
    }
  }


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

