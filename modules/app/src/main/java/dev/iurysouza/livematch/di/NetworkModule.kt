package dev.iurysouza.livematch.di

import android.util.Log
import arrow.core.continuations.either
import arrow.core.handleError
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.DefaultDispatcherProvider
import dev.iurysouza.livematch.DispatcherProvider
import dev.iurysouza.livematch.data.PolyJsonAdapterFactory
import dev.iurysouza.livematch.domain.models.reddit.entities.PremiumSubreddit
import dev.iurysouza.livematch.domain.models.reddit.entities.PrivateSubreddit
import dev.iurysouza.livematch.domain.models.reddit.entities.Redditor
import dev.iurysouza.livematch.domain.models.reddit.entities.Subreddit
import dev.iurysouza.livematch.domain.models.reddit.entities.SuspendedRedditor
import dev.iurysouza.livematch.domain.models.reddit.entities.base.RedditorData
import dev.iurysouza.livematch.domain.models.reddit.entities.base.SubredditData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedComment
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedCommentData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContribution
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedMessage
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedMoreComment
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedRedditor
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubmission
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubreddit
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
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
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(factory)
        .callbackExecutor(dispatcherProvider.io().asExecutor())
        .client(okHttpClient)

    @Provides
    @Singleton
    fun provideRedditApi(builder: Retrofit.Builder): RedditApi {
        return builder.build().create(RedditApi::class.java)
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
        if (request.url.host.contains("oauth")) {
            either.eager {
                val (value) = authStorage.getToken().bind()
                val credentials = Base64.getEncoder().encodeToString(value.toByteArray())
                request = request.newBuilder().addHeader(
                    "Authorization",
                    "Bearer: $credentials"
                ).build()
            }.handleError {
                Log.e("LiveMatch", "No Bearer Token Available $it")
            }
        }
        chain.proceed(request)
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi = Moshi.Builder().apply {
        add(
            PolymorphicJsonAdapterFactory.of(EnvelopedData::class.java, "kind")
                .withSubtype(EnvelopedComment::class.java, EnvelopeKind.Comment.value)
                .withSubtype(EnvelopedMoreComment::class.java, EnvelopeKind.More.value)
                .withSubtype(EnvelopedMessage::class.java, EnvelopeKind.Message.value)
                .withSubtype(EnvelopedRedditor::class.java, EnvelopeKind.Account.value)
                .withSubtype(EnvelopedSubmission::class.java, EnvelopeKind.Link.value)
                .withSubtype(EnvelopedSubreddit::class.java, EnvelopeKind.Subreddit.value)
        )
            .add(
                PolymorphicJsonAdapterFactory.of(EnvelopedContribution::class.java, "kind")
                    .withSubtype(EnvelopedSubmission::class.java, EnvelopeKind.Link.value)
                    .withSubtype(EnvelopedComment::class.java, EnvelopeKind.Comment.value)
                    .withSubtype(EnvelopedMoreComment::class.java, EnvelopeKind.More.value)
            )
            .add(
                PolymorphicJsonAdapterFactory.of(EnvelopedCommentData::class.java, "kind")
                    .withSubtype(EnvelopedComment::class.java, EnvelopeKind.Comment.value)
                    .withSubtype(EnvelopedMoreComment::class.java, EnvelopeKind.More.value)
            )
            .add(
                PolymorphicJsonAdapterFactory.of(SubredditData::class.java, "subreddit_type")
                    .withSubtype(PremiumSubreddit::class.java, "gold_only")
                    .withSubtype(PremiumSubreddit::class.java, "gold_restricted")
                    .withSubtype(Subreddit::class.java, "archived")
                    .withSubtype(Subreddit::class.java, "public")
                    .withSubtype(Subreddit::class.java, "restricted")
                    .withSubtype(Subreddit::class.java, "user")
                    .withSubtype(PrivateSubreddit::class.java, "private")
            )
            .add(
                PolyJsonAdapterFactory(
                    baseType = RedditorData::class.java,
                    possibleTypes = arrayOf(Redditor::class.java, SuspendedRedditor::class.java),
                    selectType = { label, value ->

                        when {
                            label == "is_suspended" && value == true -> SuspendedRedditor::class.java
                            label == "has_verified_email" -> Redditor::class.java
                            else -> null
                        }
                    }
                )
            )

    }.build()


    @Provides
    @Singleton
    internal fun provideConverterFactory(moshi: Moshi): Converter.Factory {
        return MoshiConverterFactory.create(moshi).asLenient()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    internal fun provideNetworkDataSource(redditApi: RedditApi): NetworkDataSource {
        return RedditNetworkDataSource(redditApi)
    }
}

private const val MAX_REQUESTS = 10
private const val HTTP_CONNECTION_TIMEOUT = 60
private const val NAMED_AUTH_INTERCEPTOR = "AuthInterceptor"
