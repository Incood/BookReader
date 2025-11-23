package com.example.avitotech.presentation.screens.booksDownload

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.avitotech.presentation.screens.booksDownload.components.BookUploadContent
import com.example.avitotech.presentation.screens.booksDownload.components.BookUploadTopBar
import com.example.avitotech.presentation.screens.booksDownload.state.BookUploadEvent
import com.example.avitotech.presentation.utils.getFileName

@Composable
fun BookUploadScreen(
    viewModel: BookUploadViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BookUploadTopBar()
        }
    ) { padding ->
        BookUploadContent(
            state = state,
            onEvent = { event ->
                when (event) {
                    is BookUploadEvent.FileSelected -> {
                        val fileName = getFileName(context, event.fileUri)
                        viewModel.onEvent(event.copy(fileName = fileName), context)
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            padding = padding
        )
    }
}