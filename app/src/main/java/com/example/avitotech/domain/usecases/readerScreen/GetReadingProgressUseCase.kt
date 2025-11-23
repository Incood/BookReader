package com.example.avitotech.domain.usecases.readerScreen

import com.example.avitotech.domain.repository.ReadingPreferences

class GetReadingProgressUseCase(
    private val readingPreferences: ReadingPreferences
) {
    suspend operator fun invoke(bookId: String): Float {
        return readingPreferences.getReadingProgress(bookId)
    }
}