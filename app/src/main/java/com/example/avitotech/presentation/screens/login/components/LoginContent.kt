package com.example.avitotech.presentation.screens.login.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.avitotech.presentation.screens.login.state.LoginEvent
import com.example.avitotech.presentation.screens.login.state.LoginUiState
import com.example.avitotech.presentation.utils.LoadingState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginContent(
    uiState: LoginUiState,
    paddingValues: PaddingValues,
    onEvent: (LoginEvent) -> Unit,
    onGoogleSignInClick: () -> Unit,

    ) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = uiState.isLoading,
            transitionSpec = {
                fadeIn() togetherWith fadeOut() using SizeTransform(clip = false)
            }
        ) { isLoading ->
            if (isLoading) {
                LoadingState()
            } else {
                LoginForm(
                    uiState = uiState,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                    onEvent = onEvent,
                    onGoogleSignInClick = onGoogleSignInClick
                )
            }
        }
    }
}