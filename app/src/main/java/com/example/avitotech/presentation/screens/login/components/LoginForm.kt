package com.example.avitotech.presentation.screens.login.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.avitotech.presentation.screens.login.state.LoginEvent
import com.example.avitotech.presentation.screens.login.state.LoginUiState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginForm(
    uiState: LoginUiState,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    onEvent: (LoginEvent) -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LoginHeader()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmailTextField(
                    value = uiState.email,
                    error = uiState.emailError,
                    onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                    onNext = { passwordFocusRequester.requestFocus() },
                    focusRequester = emailFocusRequester
                )

                PasswordTextField(
                    value = uiState.password,
                    error = uiState.passwordError,
                    isVisible = passwordVisible,
                    onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                    onToggleVisibility = onPasswordVisibilityToggle,
                    onDone = { focusManager.clearFocus() },
                    focusRequester = passwordFocusRequester
                )
            }

            SignInButton(
                enabled = uiState.isFormValid && !uiState.isLoading,
                onClick = { onEvent(LoginEvent.SignIn) }
            )

            OrDivider()

            GoogleSignInButton(
                onClick = onGoogleSignInClick
            )
        }
    }
}