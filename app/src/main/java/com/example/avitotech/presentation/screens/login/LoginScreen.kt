package com.example.avitotech.presentation.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.avitotech.presentation.screens.login.components.LoginContent
import com.example.avitotech.presentation.screens.login.components.LoginTopBar
import com.example.avitotech.presentation.screens.login.state.LoginEvent
import com.example.avitotech.presentation.screens.login.state.LoginNavigationEvent
import com.example.avitotech.presentation.screens.login.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val snackbarHostState = remember { SnackbarHostState() }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleGoogleSignInResult(result.resultCode, result.data)
    }

    LaunchedEffect(uiState.error) {
        if (!uiState.error.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.error!!,
                actionLabel = context.getString(R.string.repeat)
            )
            viewModel.onEvent(LoginEvent.Retry)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginNavigationEvent.NavigateToHome -> onNavigateToHome()
                is LoginNavigationEvent.NavigateToSignUp -> onNavigateToSignUp()
                is LoginNavigationEvent.LaunchGoogleSignIn -> {
                    googleSignInLauncher.launch(event.signInIntent)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LoginTopBar(onNavigateToSignUp = onNavigateToSignUp)
        }
    ) { paddingValues ->
        LoginContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onEvent = viewModel::onEvent,
            onGoogleSignInClick = { viewModel.startGoogleSignIn(activity) }
        )
    }
}