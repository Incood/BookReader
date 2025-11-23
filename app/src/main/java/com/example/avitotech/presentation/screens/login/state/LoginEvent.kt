package com.example.avitotech.presentation.screens.login.state

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object SignIn : LoginEvent()
    object NavigateToSignUp : LoginEvent()
    object Retry : LoginEvent()
}