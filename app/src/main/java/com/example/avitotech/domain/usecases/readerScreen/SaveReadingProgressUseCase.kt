package com.example.avitotech.domain.usecases.readerScreen

import com.example.avitotech.domain.repository.ReadingPreferences

class SaveReadingProgressUseCase(
    private val readingPreferences: ReadingPreferences
) {
    suspend operator fun invoke(bookId: String, progress: Float) {
        readingPreferences.saveReadingProgress(bookId, progress)
    }
}