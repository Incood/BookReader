package com.example.avitotech.domain.repository

import com.example.avitotech.domain.models.ReadingSettings

interface ReadingPreferences {
    suspend fun saveReadingProgress(bookId: String, progress: Float)
    suspend fun getReadingProgress(bookId: String): Float
    fun getReadingSettings(): ReadingSettings
    suspend fun saveReadingSettings(settings: ReadingSettings)
}