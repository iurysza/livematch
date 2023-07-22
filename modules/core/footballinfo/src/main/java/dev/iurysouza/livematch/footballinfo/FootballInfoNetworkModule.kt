package dev.iurysouza.livematch.footballinfo

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.common.DispatcherProvider
import dev.iurysouza.livematch.footballinfo.data.FootballInfoApi
import dev.iurysouza.livematch.footballinfo.data.FootballNetworkInfoSource
import dev.iurysouza.livematch.footballinfo.domain.FootballInfoSource
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.asExecutor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class FootballInfoNetworkModule {
  @Provides
  @Singleton
  @Named(FOOTBALLINFO_RETROFIT)
  internal fun provideRetrofitInfoBuilder(
    dispatcherProvider: DispatcherProvider,
    okHttpClient: OkHttpClient,
  ): Retrofit.Builder {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.FOOTBALL_INFO_BASE_URL)
      .addConverterFactory(
        MoshiConverterFactory.create(
          Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build(),
        ),
      )
      .callbackExecutor(dispatcherProvider.io().asExecutor())
      .client(okHttpClient)
  }

  @Provides
  @Singleton
  fun provideFootballInfoApi(
    @Named(FOOTBALLINFO_RETROFIT) builder: Retrofit.Builder,
  ): FootballInfoApi = builder.build().create(FootballInfoApi::class.java)

  @Provides
  @Singleton
  internal fun provideFootballInfoSource(
    api: FootballInfoApi,
  ): FootballInfoSource = FootballNetworkInfoSource(api)
}

private const val FOOTBALLINFO_RETROFIT = "FootballInfo"
