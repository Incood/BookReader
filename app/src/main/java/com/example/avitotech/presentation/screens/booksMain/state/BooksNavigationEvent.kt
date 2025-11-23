package com.example.avitotech.presentation.screens.booksMain.state

sealed class BooksNavigationEvent {
    data class OpenBookReader(val bookId: String) : BooksNavigationEvent()
}