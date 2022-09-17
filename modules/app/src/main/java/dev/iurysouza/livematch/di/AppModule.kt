package dev.iurysouza.livematch.di

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.data.storage.SystemStorage
import dev.iurysouza.livematch.domain.adapters.KeyValueStorage
import dev.iurysouza.livematch.ui.features.matchthread.MatchThreadMapper
import dev.iurysouza.livematch.util.JsonParser
import dev.iurysouza.livematch.util.MoshiJsonParser
import dev.iurysouza.livematch.util.ResourceProvider
import dev.iurysouza.livematch.util.SystemResourceProvider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideResourceProvider(@ApplicationContext appContext: Context): ResourceProvider {
        return SystemResourceProvider(appContext)
    }

    @Provides
    @Singleton
    internal fun provideSystemStorage(sharedPreferences: SharedPreferences): KeyValueStorage {
        return SystemStorage(sharedPreferences)
    }

    @Provides
    @Singleton
    internal fun provideMatchThreadMapper(): MatchThreadMapper {
        return MatchThreadMapper()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("livematch", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    internal fun providesJsonParser(moshi: Moshi): JsonParser {
        return MoshiJsonParser(moshi)
    }
}
