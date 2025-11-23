package com.example.avitotech.presentation.screens.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotech.domain.models.ReadingSettings
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.usecases.homeScreen.DeleteBookUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetBookContentUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetBookUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetReadingProgressUseCase
import com.example.avitotech.domain.usecases.readerScreen.GetReadingSettingsUseCase
import com.example.avitotech.domain.usecases.readerScreen.SaveReadingProgressUseCase
import com.example.avitotech.domain.usecases.readerScreen.SaveReadingSettingsUseCase
import com.example.avitotech.presentation.screens.reader.state.BookReaderEvent
import com.example.avitotech.presentation.screens.reader.state.BookReaderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReaderViewModel @Inject constructor(
    private val getBookContentUseCase: GetBookContentUseCase,
    private val saveReadingProgressUseCase: SaveReadingProgressUseCase,
    private val getReadingProgressUseCase: GetReadingProgressUseCase,
    private val getReadingSettingsUseCase: GetReadingSettingsUseCase,
    private val saveReadingSettingsUseCase: SaveReadingSettingsUseCase,
    private val deleteBookUseCase: DeleteBookUseCase,
    private val getBookUseCase: GetBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookReaderUiState())
    val uiState: StateFlow<BookReaderUiState> = _uiState.asStateFlow()

    private var currentBookId: String? = null

    fun onEvent(event: BookReaderEvent) {
        when (event) {
            is BookReaderEvent.LoadBook -> loadBook(event.bookId)
            is BookReaderEvent.SaveProgress -> saveProgress(event.progress)
            is BookReaderEvent.UpdateSettings -> updateSettings(event.settings)
            BookReaderEvent.ToggleSettings -> toggleSettings()
            BookReaderEvent.Retry -> retry()
            BookReaderEvent.DeleteBook -> deleteBook()
        }
    }

    private fun loadBook(bookId: String) {
        currentBookId = bookId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                when (val bookResult = getBookUseCase(bookId)) {
                    is Resource.Success -> {
                        val book = bookResult.data

                        if (!book.isDownloaded) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Локальный файл книги не найден"
                                )
                            }
                            return@launch
                        }

                        _uiState.update {
                            it.copy(
                                book = book,
                                readingSettings = getReadingSettingsUseCase(),
                                readingProgress = getReadingProgressUseCase(bookId)
                            )
                        }

                        when (val contentResult = getBookContentUseCase(bookId)) {
                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        bookContent = contentResult.data,
                                        error = null
                                    )
                                }
                            }
                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = contentResult.message
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = bookResult.message
                            )
                        }
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки: ${e.message}"
                    )
                }
            }
        }
    }

    private fun saveProgress(progress: Float) {
        viewModelScope.launch {
            currentBookId?.let { bookId ->
                saveReadingProgressUseCase(bookId, progress)
                _uiState.update { it.copy(readingProgress = progress) }
            }
        }
    }

    private fun updateSettings(settings: ReadingSettings) {
        viewModelScope.launch {
            saveReadingSettingsUseCase(settings)
            _uiState.update { it.copy(readingSettings = settings) }
        }
    }

    private fun toggleSettings() {
        _uiState.update { it.copy(isSettingsVisible = !it.isSettingsVisible) }
    }

    private fun retry() {
        currentBookId?.let { loadBook(it) }
    }

    private fun deleteBook() {
        viewModelScope.launch {
            currentBookId?.let { bookId ->
                when (deleteBookUseCase(bookId)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                error = "Книга удалена",
                                shouldNavigateBack = true
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = "Не удалось удалить книгу")
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}