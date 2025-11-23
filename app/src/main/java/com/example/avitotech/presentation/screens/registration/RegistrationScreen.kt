package com.example.avitotech.presentation.screens.registration

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.avitotech.R
import com.example.avitotech.presentation.screens.registration.components.RegistrationContent
import com.example.avitotech.presentation.screens.registration.components.RegistrationTopBar
import com.example.avitotech.presentation.screens.registration.state.RegistrationEvent
import com.example.avitotech.presentation.screens.registration.state.RegistrationNavigationEvent

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        if (!uiState.error.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.error!!,
                actionLabel = context.getString(R.string.repeat)
            )
            viewModel.onEvent(RegistrationEvent.Retry)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RegistrationNavigationEvent.NavigateToHome  -> onNavigateToHome()
                is RegistrationNavigationEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RegistrationTopBar(onNavigateToLogin = onNavigateToLogin)
        }
    ) { paddingValues ->
        RegistrationContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onEvent = viewModel::onEvent
        )
    }
}