package com.example.avitotech.di.modules.reader

import com.example.avitotech.data.repository.ReadingPreferencesImpl
import com.example.avitotech.domain.repository.ReadingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReaderDataModule {

    @Provides
    @Singleton
    fun provideReadingPreferences(
        readingPreferencesImpl: ReadingPreferencesImpl
    ): ReadingPreferences = readingPreferencesImpl
}