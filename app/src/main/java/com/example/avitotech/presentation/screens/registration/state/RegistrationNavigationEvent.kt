package com.example.avitotech.presentation.screens.registration.state

sealed class RegistrationNavigationEvent {
    object NavigateToHome : RegistrationNavigationEvent()
    object NavigateToLogin : RegistrationNavigationEvent()
}