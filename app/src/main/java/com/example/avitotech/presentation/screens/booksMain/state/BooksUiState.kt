package com.example.avitotech.presentation.screens.booksMain.state

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.BookDownloadState
import com.example.avitotech.domain.models.SortType

data class BooksUiState(
    val books: List<Book> = emptyList(),
    val filteredBooks: List<Book> = emptyList(),
    val isSearchVisible: Boolean = false,
    val searchQuery: String = "",
    val sortType: SortType = SortType.TITLE,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val isRefreshing: Boolean = false,
    val downloadStates: Map<String, BookDownloadState> = emptyMap()
)