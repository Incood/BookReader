package com.example.avitotech.presentation.screens.booksMain.state

import com.example.avitotech.domain.models.SortType

sealed class BooksEvent {
    object LoadBooks : BooksEvent()
    data class SearchBooks(val query: String) : BooksEvent()
    data class SortBooks(val sortType: SortType) : BooksEvent()
    data class DownloadBook(val bookId: String) : BooksEvent()
    data class DeleteBook(val bookId: String) : BooksEvent()
    object RefreshBooks : BooksEvent()
    object Retry : BooksEvent()
    object ClearError : BooksEvent()
}