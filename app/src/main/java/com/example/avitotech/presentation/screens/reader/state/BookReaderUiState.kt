package com.example.avitotech.presentation.screens.reader.state

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.ReadingSettings

data class BookReaderUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val bookContent: String? = null,
    val error: String? = null,
    val readingProgress: Float = 0f,
    val readingSettings: ReadingSettings = ReadingSettings(),
    val isSettingsVisible: Boolean = false,
    val shouldNavigateBack: Boolean = false
)