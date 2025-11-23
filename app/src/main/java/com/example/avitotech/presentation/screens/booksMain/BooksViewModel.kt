package com.example.avitotech.presentation.screens.booksMain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.models.SortType
import com.example.avitotech.domain.usecases.homeScreen.DownloadBookUseCase
import com.example.avitotech.domain.usecases.homeScreen.GetBooksUseCase
import com.example.avitotech.presentation.screens.booksMain.state.BooksEvent
import com.example.avitotech.presentation.screens.booksMain.state.BooksNavigationEvent
import com.example.avitotech.presentation.screens.booksMain.state.BooksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val downloadBookUseCase: DownloadBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<BooksNavigationEvent>()
    val navigationEvent: SharedFlow<BooksNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    val searchQuery: String
        get() = _uiState.value.searchQuery

    val isSearchVisible: Boolean
        get() = _uiState.value.isSearchVisible

    init {
        loadBooks()
    }

    fun onEvent(event: BooksEvent) {
        when (event) {
            is BooksEvent.LoadBooks -> loadBooks()
            is BooksEvent.SearchBooks -> searchBooks(event.query)
            is BooksEvent.SortBooks -> sortBooks(event.sortType)
            is BooksEvent.DownloadBook -> downloadBook(event.bookId)
            is BooksEvent.DeleteBook -> deleteBook(event.bookId)
            is BooksEvent.RefreshBooks -> refreshBooks()
            is BooksEvent.Retry -> loadBooks()
            is BooksEvent.ClearError -> clearError()
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchBooks(query)
    }

    fun onSearchVisibleChange(isVisible: Boolean) {
        _uiState.update { it.copy(isSearchVisible = isVisible) }
    }

    fun onSortClick() {
        val currentSortType = _uiState.value.sortType
        val nextSortType = when (currentSortType) {
            SortType.TITLE -> SortType.DATE
            SortType.DATE -> SortType.TITLE
        }
        onEvent(BooksEvent.SortBooks(nextSortType))
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = getBooksUseCase()) {
                is Resource.Success -> {
                    val books = result.data
                    _uiState.update {
                        it.copy(
                            books = books,
                            filteredBooks = filterAndSortBooks(books, it.searchQuery, it.sortType),
                            isLoading = false,
                            isEmpty = books.isEmpty(),
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun refreshBooks() {
        viewModelScope.launch {
            loadBooks()
        }
    }

    private fun searchBooks(query: String) {
        _uiState.update { state ->
            state.copy(
                filteredBooks = filterAndSortBooks(state.books, query, state.sortType)
            )
        }
    }

    private fun sortBooks(sortType: SortType) {
        _uiState.update { state ->
            state.copy(
                sortType = sortType,
                filteredBooks = filterAndSortBooks(state.books, state.searchQuery, sortType)
            )
        }

        viewModelScope.launch {
            _toastMessage.emit("Sorted by: ${sortType.name.lowercase()}")
        }
    }

    private fun downloadBook(bookId: String) {}

    private fun deleteBook(bookId: String) {
        viewModelScope.launch {
            _toastMessage.emit("Deleting book $bookId")
            _uiState.update { state ->
                state.copy(
                    books = state.books.filter { it.id != bookId },
                    filteredBooks = state.filteredBooks.filter { it.id != bookId },
                    downloadStates = state.downloadStates - bookId
                )
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun filterAndSortBooks(
        books: List<Book>,
        query: String,
        sortType: SortType
    ): List<Book> {
        val filtered = if (query.isBlank()) {
            books
        } else {
            books.filter { book ->
                book.title.contains(query, ignoreCase = true) ||
                    book.author.contains(query, ignoreCase = true)
            }
        }

        return when (sortType) {
            SortType.TITLE -> filtered.sortedBy { it.title }
            SortType.DATE -> filtered.sortedByDescending { it.downloadDate }
        }
    }
}