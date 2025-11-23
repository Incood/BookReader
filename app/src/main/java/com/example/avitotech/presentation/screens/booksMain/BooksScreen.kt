package com.example.avitotech.presentation.screens.booksMain

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.avitotech.presentation.screens.booksMain.components.AnimatedSearchOverlay
import com.example.avitotech.presentation.screens.booksMain.components.BooksContent
import com.example.avitotech.presentation.screens.booksMain.components.BooksTopBar
import com.example.avitotech.presentation.screens.booksMain.state.BooksEvent
import com.example.avitotech.presentation.screens.booksMain.state.BooksNavigationEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    onNavigateToReader: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is BooksNavigationEvent.OpenBookReader -> onNavigateToReader(event.bookId)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.error) {
        if (!uiState.error.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.error!!,
                actionLabel = "Повторить"
            )
            viewModel.onEvent(BooksEvent.Retry)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            BooksTopBar(
                searchQuery = viewModel.searchQuery,
                onSearchQueryChange = { viewModel.onEvent(BooksEvent.SearchBooks(it)) },
                onSortClick = viewModel::onSortClick,
                isSearchVisible = viewModel.isSearchVisible,
                onSearchVisibleChange = viewModel::onSearchVisibleChange,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        BooksContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onEvent = viewModel::onEvent,
            onBookClick = { bookId ->
                onNavigateToReader(bookId)
            }
        )
    }

    AnimatedSearchOverlay(
        searchQuery = viewModel.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        isVisible = viewModel.isSearchVisible,
        modifier = Modifier.padding(top = 40.dp, start = 16.dp, end = 16.dp)
    )
}