package com.example.avitotech.presentation.screens.booksMain.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avitotech.presentation.screens.booksMain.state.BooksEvent
import com.example.avitotech.presentation.screens.booksMain.state.BooksUiState
import com.example.avitotech.presentation.utils.LoadingState

@Composable
fun BooksContent(
    uiState: BooksUiState,
    paddingValues: PaddingValues,
    onEvent: (BooksEvent) -> Unit,
    onBookClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when {
            uiState.isLoading -> LoadingState()
            uiState.isEmpty -> EmptyState(onRefresh = { onEvent(BooksEvent.RefreshBooks) })
            uiState.error != null -> ErrorState(
                error = uiState.error,
                onRetry = { onEvent(BooksEvent.Retry) }
            )
            else -> BooksList(
                books = uiState.filteredBooks,
                downloadStates = uiState.downloadStates,
                onBookClick = onBookClick,
                onDownloadClick = { onEvent(BooksEvent.DownloadBook(it)) },
                onDeleteClick = { onEvent(BooksEvent.DeleteBook(it)) },
            )
        }
    }
}