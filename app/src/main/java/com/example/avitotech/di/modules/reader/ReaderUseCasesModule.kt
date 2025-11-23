package com.example.avitotech.di.modules.reader

import com.example.avitotech.domain.repository.BookRepository
import com.example.avitotech.domain.repository.ReadingPreferences
import com.example.avitotech.domain.usecases.readerScreen.GetBookContentUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetBookUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetReadingProgressUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetReadingSettingsUseCase
import com.example.avitotech.domain.usecases.readerScreen.SaveReadingProgressUseCase
import com.example.avitotech.domain.usecases.readerScreen.SaveReadingSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ReaderUseCasesModule {

    @Provides
    fun provideGetBookUseCase(bookRepository: BookRepository): GetBookUseCase =
        GetBookUseCase(bookRepository)

    @Provides
    fun provideGetBookContentUseCase(bookRepository: BookRepository): GetBookContentUseCase =
        GetBookContentUseCase(bookRepository)

    @Provides
    fun provideSaveReadingProgressUseCase(
        readingPreferences: ReadingPreferences
    ): SaveReadingProgressUseCase = SaveReadingProgressUseCase(readingPreferences)

    @Provides
    fun provideGetReadingProgressUseCase(
        readingPreferences: ReadingPreferences
    ): GetReadingProgressUseCase = GetReadingProgressUseCase(readingPreferences)

    @Provides
    fun provideGetReadingSettingsUseCase(
        readingPreferences: ReadingPreferences
    ): GetReadingSettingsUseCase = GetReadingSettingsUseCase(readingPreferences)

    @Provides
    fun provideSaveReadingSettingsUseCase(
        readingPreferences: ReadingPreferences
    ): SaveReadingSettingsUseCase = SaveReadingSettingsUseCase(readingPreferences)
}