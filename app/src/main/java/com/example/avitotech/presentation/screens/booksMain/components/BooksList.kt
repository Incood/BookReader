package com.example.avitotech.presentation.screens.booksMain.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.BookDownloadState

@Composable
fun BooksList(
    books: List<Book>,
    downloadStates: Map<String, BookDownloadState>,
    onBookClick: (String) -> Unit,
    onDownloadClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = books,
            key = { it.id }
        ) { book ->
            BookItem(
                book = book,
                downloadState = downloadStates[book.id],
                onBookClick = { onBookClick(book.id) },
                onActionClick = {
                    if (book.isDownloaded) {
                        onDeleteClick(book.id)
                    } else {
                        onDownloadClick(book.id)
                    }
                }
            )
        }
    }
}