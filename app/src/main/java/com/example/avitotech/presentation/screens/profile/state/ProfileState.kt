package com.example.avitotech.presentation.screens.profile.state

import com.example.avitotech.domain.models.User

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}