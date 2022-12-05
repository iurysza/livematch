package dev.iurysouza.livematch.matchthread

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MatchThreadModule {

    @Provides
    @Singleton
    internal fun provideMatchThreadMapper(): MatchEventParser = MatchEventParser()

}
