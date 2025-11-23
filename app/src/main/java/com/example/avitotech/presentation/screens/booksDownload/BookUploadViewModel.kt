package com.example.avitotech.presentation.screens.booksDownload

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.usecases.downloadScreen.UploadBookUseCase
import com.example.avitotech.domain.usecases.downloadScreen.ValidateFileUseCase
import com.example.avitotech.presentation.screens.booksDownload.state.BookUploadEvent
import com.example.avitotech.presentation.screens.booksDownload.state.BookUploadState
import com.example.avitotech.presentation.utils.getFileName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookUploadViewModel @Inject constructor(
    private val uploadBookUseCase: UploadBookUseCase,
    private val validateBookFileUseCase: ValidateFileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookUploadState())
    val state: StateFlow<BookUploadState> = _state.asStateFlow()

    fun onEvent(event: BookUploadEvent, context: Context? = null) {
        when (event) {
            is BookUploadEvent.TitleChanged -> {
                _state.value = _state.value.copy(title = event.title)
            }
            is BookUploadEvent.AuthorChanged -> {
                _state.value = _state.value.copy(author = event.author)
            }
            is BookUploadEvent.FileSelected -> {
                val fileName = event.fileName.ifEmpty {
                    context?.let { getFileName(it, event.fileUri) } ?: "unknown_file"
                }
                _state.value = _state.value.copy(
                    selectedFileUri = event.fileUri,
                    fileName = fileName
                )
            }
            is BookUploadEvent.UploadBook -> {
                uploadBook()
            }
            is BookUploadEvent.ValidateFile -> {
                validateFile()
            }
            is BookUploadEvent.ResetState -> {
                resetState()
            }
        }
    }

    private fun uploadBook() {
        val currentState = _state.value
        val fileUri = currentState.selectedFileUri
        val title = currentState.title
        val author = currentState.author

        if (fileUri == null || title.isBlank() || author.isBlank()) {
            _state.value = currentState.copy(
                uploadState = Resource.Error("Please fill all fields and select a file")
            )
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(
                isUploading = true,
                uploadState = Resource.Loading,
                progress = 0.3f
            )

            val book = Book(title = title, author = author)

            uploadBookUseCase(book, fileUri).collect { result ->
                _state.value = _state.value.copy(
                    uploadState = result,
                    isUploading = result is Resource.Loading,
                    progress = when (result) {
                        is Resource.Loading -> 0.7f
                        is Resource.Success -> 1.0f
                        is Resource.Error -> 0f
                    }
                )
            }
        }
    }

    private fun validateFile() {
        val fileUri = _state.value.selectedFileUri
        if (fileUri == null) {
            _state.value = _state.value.copy(
                validationState = Resource.Error("Please select a file first")
            )
            return
        }

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    validationState = Resource.Loading
                )

                val result = validateBookFileUseCase(fileUri)

                _state.value = _state.value.copy(
                    validationState = result
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    validationState = Resource.Error("Validation failed: ${e.message}")
                )
            }
        }
    }

    private fun resetState() {
        _state.value = BookUploadState()
    }
}