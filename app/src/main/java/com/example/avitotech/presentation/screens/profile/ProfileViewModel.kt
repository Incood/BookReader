package com.example.avitotech.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.usecases.profile.GetCurrentUserUseCase
import com.example.avitotech.domain.usecases.profile.SignOutUseCase
import com.example.avitotech.domain.usecases.profile.UpdateUserProfileUseCase
import com.example.avitotech.domain.usecases.profile.UploadProfileImageUseCase
import com.example.avitotech.presentation.screens.profile.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _signOutResult = MutableStateFlow<Result<Unit>?>(null)
    val signOutResult: StateFlow<Result<Unit>?> = _signOutResult.asStateFlow()

    init {
        println("DEBUG: ProfileViewModel init")
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                println("DEBUG: Loading user profile...")
                val user = getCurrentUserUseCase()
                println("DEBUG: User loaded: $user")
                if (user != null) {
                    _profileState.value = ProfileState.Success(user)
                    println("DEBUG: ProfileState set to Success")
                } else {
                    _profileState.value = ProfileState.Error("Пользователь не найден")
                    println("DEBUG: ProfileState set to Error - user null")
                }
            } catch (e: Exception) {
                println("DEBUG: Error loading profile: ${e.message}")
                _profileState.value = ProfileState.Error("Ошибка загрузки профиля: ${e.message}")
            }
        }
    }

    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            val currentState = _profileState.value
            when (currentState) {
                is ProfileState.Success -> {
                    _profileState.value = currentState.copy(user = user)
                    updateUserProfileUseCase(user).onFailure { error ->
                        _profileState.value = ProfileState.Error("Ошибка обновления: ${error.message}")
                        loadUserProfile()
                    }
                }
                is ProfileState.Loading -> {
                    _profileState.value = ProfileState.Error("Профиль еще загружается")
                }
                is ProfileState.Error -> {
                    _profileState.value = ProfileState.Error("Нельзя обновить профиль в состоянии ошибки")
                }
            }
        }
    }

    fun uploadProfileImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            uploadProfileImageUseCase(imageBytes).onSuccess { imageUrl ->
                val currentState = _profileState.value
                when (currentState) {
                    is ProfileState.Success -> {
                        val updatedUser = currentState.user.copy(profilePicture = imageUrl)
                        updateUserProfile(updatedUser)
                    }
                    is ProfileState.Loading -> {
                        _profileState.value = ProfileState.Error("Профиль еще загружается")
                    }
                    is ProfileState.Error -> {
                        _profileState.value = ProfileState.Error("Нельзя обновить фото в состоянии ошибки")
                    }
                }
            }.onFailure { error ->
                _profileState.value = ProfileState.Error("Ошибка загрузки фото: ${error.message}")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _signOutResult.value = signOutUseCase()
        }
    }

    fun clearSignOutResult() {
        _signOutResult.value = null
    }
}