package dev.iurysouza.livematch.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

  @Provides
  @Singleton
  @Suppress("MagicNumber")
  fun provideHttpDelayInterceptor() = Interceptor { chain ->
    val delay: Long = 1000
    Thread.sleep(delay)
    Timber.w("=============Delaying request by ${delay}ms=============")
    chain.proceed(chain.request())
  }

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    delayer: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor,
  ): OkHttpClient = OkHttpClient.Builder()
    .dispatcher(Dispatcher().apply { maxRequestsPerHost = MAX_REQUESTS })
    .connectTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
    .readTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
    .apply { if (BuildConfig.USE_MOCK_URL) addInterceptor(delayer) }
    .apply { if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor) }
    .build()
}

private const val MAX_REQUESTS = 10
private const val HTTP_CONNECTION_TIMEOUT = 60
