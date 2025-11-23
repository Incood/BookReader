package com.example.avitotech.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotech.domain.models.ValidationResult
import com.example.avitotech.domain.usecases.auth.GetCurrentUserUseCase
import com.example.avitotech.domain.usecases.auth.SignUpUseCase
import com.example.avitotech.domain.usecases.auth.ValidateEmailUseCase
import com.example.avitotech.domain.usecases.auth.ValidatePasswordUseCase
import com.example.avitotech.presentation.screens.registration.state.RegistrationEvent
import com.example.avitotech.presentation.screens.registration.state.RegistrationNavigationEvent
import com.example.avitotech.presentation.screens.registration.state.RegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RegistrationNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        val currentUser = getCurrentUserUseCase()
        if (currentUser != null) {
            viewModelScope.launch {
                _navigationEvent.emit(RegistrationNavigationEvent.NavigateToHome)
            }
        }
    }

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.EmailChanged           -> updateEmail(event.email)
            is RegistrationEvent.PasswordChanged        -> updatePassword(event.password)
            is RegistrationEvent.ConfirmPasswordChanged -> updateConfirmPassword(event.confirmPassword)
            is RegistrationEvent.NameChanged            -> updateName(event.name)
            is RegistrationEvent.SignUp                 -> signUp()
            is RegistrationEvent.NavigateToLogin        -> navigateToLogin()
            is RegistrationEvent.Retry                  -> clearError()
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
        validateForm()
    }

    private fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
        validateForm()
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
        validateForm()
    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
        validateForm()
    }

    private fun validateForm() {
        val emailValidation = validateEmailUseCase(_uiState.value.email)
        val passwordValidation = validatePasswordUseCase(_uiState.value.password)
        val confirmPasswordValidation = validateConfirmPassword(
            _uiState.value.password,
            _uiState.value.confirmPassword
        )
        val nameValidation = validateName(_uiState.value.name)

        _uiState.update {
            it.copy(
                isFormValid = emailValidation.successful &&
                    passwordValidation.successful &&
                    confirmPasswordValidation.successful &&
                    nameValidation.successful,
                emailError = emailValidation.errorMessage,
                passwordError = passwordValidation.errorMessage,
                confirmPasswordError = confirmPasswordValidation.errorMessage,
                nameError = nameValidation.errorMessage
            )
        }
    }

    private fun signUp() {
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = signUpUseCase(
                _uiState.value.email,
                _uiState.value.password
            )

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _navigationEvent.emit(RegistrationNavigationEvent.NavigateToHome)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Неизвестная ошибка регистрации",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(RegistrationNavigationEvent.NavigateToLogin)
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult(
                successful = false,
                errorMessage = "Подтверждение пароля не может быть пустым"
            )
            password != confirmPassword -> ValidationResult(
                successful = false,
                errorMessage = "Пароли не совпадают"
            )
            else -> ValidationResult(successful = true)
        }
    }

    private fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(
                successful = false,
                errorMessage = "Имя не может быть пустым"
            )
            name.length < 2 -> ValidationResult(
                successful = false,
                errorMessage = "Имя должно содержать минимум 2 символа"
            )
            else -> ValidationResult(successful = true)
        }
    }
}