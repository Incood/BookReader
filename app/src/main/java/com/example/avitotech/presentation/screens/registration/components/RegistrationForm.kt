package com.example.avitotech.presentation.screens.registration.components

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
import com.example.avitotech.presentation.screens.login.components.EmailTextField
import com.example.avitotech.presentation.screens.login.components.PasswordTextField
import com.example.avitotech.presentation.screens.registration.state.RegistrationEvent
import com.example.avitotech.presentation.screens.registration.state.RegistrationUiState

@Composable
fun RegistrationForm(
    uiState: RegistrationUiState,
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    onEvent: (RegistrationEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val nameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        RegistrationHeader()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NameTextField(
                    value = uiState.name,
                    error = uiState.nameError,
                    onValueChange = { onEvent(RegistrationEvent.NameChanged(it)) },
                    onNext = { emailFocusRequester.requestFocus() },
                    focusRequester = nameFocusRequester
                )

                EmailTextField(
                    value = uiState.email,
                    error = uiState.emailError,
                    onValueChange = { onEvent(RegistrationEvent.EmailChanged(it)) },
                    onNext = { passwordFocusRequester.requestFocus() },
                    focusRequester = emailFocusRequester
                )

                PasswordTextField(
                    value = uiState.password,
                    error = uiState.passwordError,
                    isVisible = passwordVisible,
                    onValueChange = { onEvent(RegistrationEvent.PasswordChanged(it)) },
                    onToggleVisibility = onPasswordVisibilityToggle,
                    onDone = { confirmPasswordFocusRequester.requestFocus() },
                    focusRequester = passwordFocusRequester,
                )

                ConfirmPasswordTextField(
                    value = uiState.confirmPassword,
                    error = uiState.confirmPasswordError,
                    isVisible = confirmPasswordVisible,
                    onValueChange = { onEvent(RegistrationEvent.ConfirmPasswordChanged(it)) },
                    onToggleVisibility = onConfirmPasswordVisibilityToggle,
                    onDone = { focusManager.clearFocus() },
                    focusRequester = confirmPasswordFocusRequester
                )
            }

            SignUpButton(
                enabled = uiState.isFormValid && !uiState.isLoading,
                onClick = { onEvent(RegistrationEvent.SignUp) }
            )
        }
    }
}