package dev.iurysouza.livematch.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.data.storage.SystemStorage
import dev.iurysouza.livematch.util.AndroidResourceProvider
import dev.iurysouza.livematch.util.ResourceProvider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(activity: Activity): SystemStorage {
        return SystemStorage(activity)
    }

    @Provides
    @Singleton
    internal fun provideContext(@ApplicationContext appContext: Context): ResourceProvider {
        return AndroidResourceProvider(appContext)
    }
}
