package dev.iurysouza.livematch.common

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.common.storage.KeyValueStorage
import dev.iurysouza.livematch.common.storage.SystemStorage
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoreModule {

  @Provides
  @Singleton
  internal fun provideResourceProvider(
    @ApplicationContext appContext: Context,
  ): ResourceProvider = SystemResourceProvider(appContext)

  @Singleton
  @Provides
  fun provideSharedPreference(
    @ApplicationContext context: Context,
  ): SharedPreferences = context.getSharedPreferences("livematch", Context.MODE_PRIVATE)

  @Provides
  @Singleton
  internal fun provideSystemStorage(
    sharedPreferences: SharedPreferences,
  ): KeyValueStorage = SystemStorage(sharedPreferences)

  @Provides
  @Singleton
  fun provideCoroutineContextProvider(): DispatcherProvider = DefaultDispatcherProvider()

  @Provides
  @Singleton
  internal fun providesJsonParser(moshi: Moshi): JsonParser = MoshiJsonParser(moshi)
}
