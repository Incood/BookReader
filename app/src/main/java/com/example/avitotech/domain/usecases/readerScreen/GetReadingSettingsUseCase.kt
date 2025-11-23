package com.example.avitotech.domain.usecases.readerScreen

import com.example.avitotech.domain.models.ReadingSettings
import com.example.avitotech.domain.repository.ReadingPreferences
import javax.inject.Inject

class GetReadingSettingsUseCase(
    private val readingPreferences: ReadingPreferences
) {
    operator fun invoke(): ReadingSettings {
        return readingPreferences.getReadingSettings()
    }
}