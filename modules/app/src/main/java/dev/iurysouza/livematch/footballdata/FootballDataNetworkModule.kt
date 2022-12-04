package dev.iurysouza.livematch.footballdata

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.core.DispatcherProvider
import dev.iurysouza.livematch.footballdata.data.FootballDataApi
import dev.iurysouza.livematch.footballdata.domain.FootballDataSource
import dev.iurysouza.livematch.footballdata.data.FootballNetworkDataSource
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.asExecutor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class FootballDataNetworkModule {

    @Provides
    @Singleton
    @Named(FOOTBALLDATA_RETROFIT)
    internal fun provideRetrofitBuilder(
        dispatcherProvider: DispatcherProvider,
        loggingInterceptor: HttpLoggingInterceptor,
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.FOOTBALL_DATA_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .callbackExecutor(dispatcherProvider.io().asExecutor())
        .client(
            okHttpClient.newBuilder().addInterceptor(loggingInterceptor).build()
        )

    @Provides
    @Singleton
    fun provideFootballDataApi(@Named(FOOTBALLDATA_RETROFIT) builder: Retrofit.Builder): FootballDataApi {
        return builder.build().create(FootballDataApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideFootballDataSource(api: FootballDataApi): FootballDataSource {
        return FootballNetworkDataSource(api)
    }

}

private const val MAX_REQUESTS = 10
private const val HTTP_CONNECTION_TIMEOUT = 60
private const val FOOTBALLDATA_RETROFIT = "FootballData"
