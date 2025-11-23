package com.example.avitotech.domain.usecases.readerScreen

import com.example.avitotech.domain.models.ReadingSettings
import com.example.avitotech.domain.repository.ReadingPreferences
import javax.inject.Inject

class SaveReadingSettingsUseCase(
    private val readingPreferences: ReadingPreferences
) {
    suspend operator fun invoke(settings: ReadingSettings) {
        readingPreferences.saveReadingSettings(settings)
    }
}