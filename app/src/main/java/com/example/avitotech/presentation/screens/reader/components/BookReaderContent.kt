package com.example.avitotech.presentation.screens.reader.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avitotech.domain.models.ReadingSettings
import com.example.avitotech.presentation.screens.booksMain.components.LoadingState
import com.example.avitotech.presentation.screens.reader.state.BookReaderEvent
import com.example.avitotech.presentation.screens.reader.state.BookReaderUiState

@Composable
fun BookReaderContent(
    uiState: BookReaderUiState,
    paddingValues: PaddingValues,
    onEvent: (BookReaderEvent) -> Unit,
    onScrollProgress: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when {
            uiState.isLoading -> LoadingState()
            uiState.error != null -> ErrorState(
                error = uiState.error,
                onRetry = { onEvent(BookReaderEvent.Retry) },
                onDelete = { onEvent(BookReaderEvent.DeleteBook) }
            )
            uiState.bookContent != null && uiState.book != null -> {
                when (uiState.book.format.lowercase()) {
                    "txt", "epub" -> TextBookViewer(
                        content = uiState.bookContent,
                        settings = uiState.readingSettings,
                        onScrollProgress = onScrollProgress
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun TextBookViewer(
    content: String,
    settings: ReadingSettings,
    onScrollProgress: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.value) {
        val maxValue = scrollState.maxValue.toFloat()
        if (maxValue > 0) {
            val progress = scrollState.value.toFloat() / maxValue
            onScrollProgress(progress)
        }
    }

    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = getFontSize(settings.fontSize),
            lineHeight = getLineHeight(settings.fontSize, settings.lineSpacing)
        ),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    )
}