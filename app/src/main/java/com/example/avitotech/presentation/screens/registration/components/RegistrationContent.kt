package com.example.avitotech.presentation.screens.registration.components

import androidx.compose.animation.AnimatedContent
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
import com.example.avitotech.presentation.screens.registration.state.RegistrationEvent
import com.example.avitotech.presentation.screens.registration.state.RegistrationUiState
import com.example.avitotech.presentation.utils.LoadingState

@Composable
fun RegistrationContent(
    uiState: RegistrationUiState,
    paddingValues: PaddingValues,
    onEvent: (RegistrationEvent) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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
                RegistrationForm(
                    uiState = uiState,
                    passwordVisible = passwordVisible,
                    confirmPasswordVisible = confirmPasswordVisible,
                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                    onConfirmPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                    onEvent = onEvent
                )
            }
        }
    }
}