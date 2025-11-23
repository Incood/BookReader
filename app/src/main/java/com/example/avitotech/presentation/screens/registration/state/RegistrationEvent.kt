package com.example.avitotech.presentation.screens.registration.state

sealed class RegistrationEvent {
    data class NameChanged(val name: String) : RegistrationEvent()
    data class EmailChanged(val email: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationEvent()
    object SignUp : RegistrationEvent()
    object NavigateToLogin : RegistrationEvent()
    object Retry : RegistrationEvent()
}