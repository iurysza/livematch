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
import okhttp3.Interceptor
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
    internal fun provideContext(@ApplicationContext appContext: Context): ResourceProvider {
        return AndroidResourceProvider(appContext)
    }
}
