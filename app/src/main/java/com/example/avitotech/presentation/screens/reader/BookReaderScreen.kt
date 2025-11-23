package com.example.avitotech.presentation.screens.reader

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.avitotech.R
import com.example.avitotech.presentation.screens.reader.components.BookReaderContent
import com.example.avitotech.presentation.screens.reader.components.PdfRendererScreen
import com.example.avitotech.presentation.screens.reader.components.ReaderTopBar
import com.example.avitotech.presentation.screens.reader.components.ReadingProgressBar
import com.example.avitotech.presentation.screens.reader.components.ReadingSettingsBottomSheet
import com.example.avitotech.presentation.screens.reader.state.BookReaderEvent
import com.example.avitotech.presentation.screens.reader.state.BookReaderUiState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookReaderScreen(
    bookId: String,
    onNavigateBack: () -> Unit,
    viewModel: BookReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val showPdfRenderer = remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        viewModel.onEvent(BookReaderEvent.LoadBook(bookId))
    }

    LaunchedEffect(uiState.book) {
        uiState.book?.let { book ->
            if (book.format.equals("pdf", ignoreCase = true)) {
                showPdfRenderer.value = true
            }
        }
    }

    if (showPdfRenderer.value && uiState.book?.filePath != null) {
        PdfRendererScreen(
            file = File(uiState.book!!.filePath),
            onNavigateBack = onNavigateBack
        )
    } else {
        StandardBookReaderScreen(
            uiState = uiState,
            onNavigateBack = onNavigateBack,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardBookReaderScreen(
    uiState: BookReaderUiState,
    onNavigateBack: () -> Unit,
    viewModel: BookReaderViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            ReaderTopBar(
                title = uiState.book?.title ?: stringResource(R.string.read),
                onBackClick = onNavigateBack,
                onSettingsClick = { viewModel.onEvent(BookReaderEvent.ToggleSettings) },
                progress = uiState.readingProgress
            )
        },
        bottomBar = {
            ReadingProgressBar(progress = uiState.readingProgress)
        }
    ) { paddingValues ->
        BookReaderContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onEvent = viewModel::onEvent,
            onScrollProgress = { progress ->
                viewModel.onEvent(BookReaderEvent.SaveProgress(progress))
            }
        )

        if (uiState.isSettingsVisible) {
            ReadingSettingsBottomSheet(
                settings = uiState.readingSettings,
                onSettingsChange = { newSettings ->
                    viewModel.onEvent(BookReaderEvent.UpdateSettings(newSettings))
                },
                onDismiss = { viewModel.onEvent(BookReaderEvent.ToggleSettings) }
            )
        }
    }
}