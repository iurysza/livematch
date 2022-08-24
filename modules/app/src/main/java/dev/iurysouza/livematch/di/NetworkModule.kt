package dev.iurysouza.livematch.di

import android.util.Log
import arrow.core.continuations.either
import arrow.core.handleError
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.DefaultDispatcherProvider
import dev.iurysouza.livematch.DispatcherProvider
import dev.iurysouza.livematch.data.PlaceHolderApi
import dev.iurysouza.livematch.data.network.RedditApi
import dev.iurysouza.livematch.data.network.RedditNetworkDataSource
import dev.iurysouza.livematch.domain.adapters.NetworkDataSource
import dev.iurysouza.livematch.domain.auth.AuthStorage
import java.util.Base64
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.asExecutor
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideRetrofitBuilder(
        dispatcherProvider: DispatcherProvider,
        okHttpClient: OkHttpClient,
        factory: Converter.Factory,
    ) =
        Retrofit.Builder()
            .baseUrl("https://www.oauth.reddit.com/")
            .addConverterFactory(factory)
            .callbackExecutor(dispatcherProvider.io().asExecutor())
            .client(okHttpClient)

    @Provides
    @Singleton
    fun provideRedditApi(builder: Retrofit.Builder): RedditApi {
        return builder.build().create(RedditApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitApi(builder: Retrofit.Builder): PlaceHolderApi {
        return builder.build().create(PlaceHolderApi::class.java)
    }

    @Provides
    fun provideCoroutineContextProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named(NAMED_AUTH_INTERCEPTOR) authInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .dispatcher(Dispatcher().apply { maxRequestsPerHost = MAX_REQUESTS })
            .connectTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @Named(NAMED_AUTH_INTERCEPTOR)
    fun provideAuthInterceptor(
        authStorage: AuthStorage,
    ): Interceptor = Interceptor { chain ->
        var request = chain.request()
        if (request.header("Authorization") == null) {
            either.eager {
                val (value) = authStorage.getToken().bind()
                val credentials = Base64.getEncoder().encodeToString(value.toByteArray())
                request = request.newBuilder()
                    .addHeader("Bearer", credentials)
                    .build()
            }.handleError {
                Log.e("LiveMatch", "No Bearer Token Available $it")
            }
        }
        chain.proceed(request)
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()


    @Provides
    @Singleton
    internal fun provideConverterFactory(moshi: Moshi): Converter.Factory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Named("BasicAuthCredentials")
    internal fun provideEncodedBasicAuthCredentials(): String {
        val username = BuildConfig.CLIENT_ID
        val password = ""
        return "Basic ${
            Base64.getEncoder().encodeToString(
                "$username:$password".toByteArray()
            )
        }"
    }

    @Provides
    @Singleton
    internal fun provideNetworkDataSource(redditApi: RedditApi): NetworkDataSource {
        return RedditNetworkDataSource(redditApi)
    }
}

private const val MAX_REQUESTS = 10
private const val HTTP_CONNECTION_TIMEOUT = 60

private const val NAMED_BASIC_AUTH_CREDENTIALS = "BasicAuthCredentials"
private const val NAMED_AUTH_INTERCEPTOR = "AuthInterceptor"
