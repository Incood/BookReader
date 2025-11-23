package com.example.avitotech.presentation.screens.reader.state

import com.example.avitotech.domain.models.ReadingSettings

sealed class BookReaderEvent {
    data class LoadBook(val bookId: String) : BookReaderEvent()
    data class SaveProgress(val progress: Float) : BookReaderEvent()
    data class UpdateSettings(val settings: ReadingSettings) : BookReaderEvent()
    object ToggleSettings : BookReaderEvent()
    object Retry : BookReaderEvent()
    object DeleteBook : BookReaderEvent()
}