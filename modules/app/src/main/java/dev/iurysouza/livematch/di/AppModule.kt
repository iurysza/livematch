package dev.iurysouza.livematch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.iurysouza.livematch.features.matchthread.MatchEventParser
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    internal fun provideMatchThreadMapper(): MatchEventParser = MatchEventParser()

}
