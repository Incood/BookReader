package com.example.avitotech.presentation.screens.login.state

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val error: String? = null
)