package com.example.avitotech.presentation.screens.login.state

import android.content.Intent

sealed class LoginNavigationEvent {
    object NavigateToHome : LoginNavigationEvent()
    object NavigateToSignUp : LoginNavigationEvent()
    data class LaunchGoogleSignIn(val signInIntent: Intent) : LoginNavigationEvent()
}