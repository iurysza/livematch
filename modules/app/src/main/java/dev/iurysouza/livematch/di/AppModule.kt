package dev.iurysouza.livematch.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.DefaultDispatcherProvider
import dev.iurysouza.livematch.DispatcherProvider
import dev.iurysouza.livematch.data.PlaceHolderApi
import dev.iurysouza.livematch.data.RedditApi
import dev.iurysouza.livematch.util.AndroidResourceProvider
import dev.iurysouza.livematch.util.JsonParser
import dev.iurysouza.livematch.util.MoshiJsonParser
import dev.iurysouza.livematch.util.ResourceProvider
import java.util.Base64
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideRetrofitBuilder(okHttpClient: OkHttpClient, factory: Converter.Factory) =
        Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(factory)
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
        @Named("BasicAuthCredentials") credentials: String,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .dispatcher(Dispatcher().apply { maxRequestsPerHost = MAX_REQUESTS })
            .connectTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", credentials).build()
                )
            }
            .addInterceptor(loggingInterceptor).build()

    @Provides
    @Singleton
    internal fun provideContext(@ApplicationContext appContext: Context): ResourceProvider {
        return AndroidResourceProvider(appContext)
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()


    @Provides
    @Singleton
    internal fun providesJsonParse(moshi: Moshi): JsonParser {
        return MoshiJsonParser(moshi)
    }

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

    companion object {
        private const val MAX_REQUESTS = 10
        private const val HTTP_CONNECTION_TIMEOUT = 60

        private const val API_URL = BuildConfig.API_URL
    }
}
