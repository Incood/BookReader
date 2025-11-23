package com.example.avitotech.presentation.screens.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotech.domain.usecases.auth.GetCurrentUserUseCase
import com.example.avitotech.domain.usecases.auth.SignInUseCase
import com.example.avitotech.domain.usecases.auth.SignInWithGoogleUseCase
import com.example.avitotech.domain.usecases.auth.StartGoogleSignInUseCase
import com.example.avitotech.domain.usecases.auth.ValidateEmailUseCase
import com.example.avitotech.domain.usecases.auth.ValidatePasswordUseCase
import com.example.avitotech.presentation.screens.login.state.LoginEvent
import com.example.avitotech.presentation.screens.login.state.LoginNavigationEvent
import com.example.avitotech.presentation.screens.login.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val startGoogleSignInUseCase: StartGoogleSignInUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        checkAuthLogin()
    }

    private fun checkAuthLogin() {
        val currentUser = getCurrentUserUseCase()
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged     -> updateEmail(event.email)
            is LoginEvent.PasswordChanged  -> updatePassword(event.password)
            is LoginEvent.Retry            -> clearError()
            is LoginEvent.SignIn           -> signIn()

            is LoginEvent.NavigateToSignUp -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToSignUp)
                }
            }
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

    private fun validateForm() {
        val emailValidation = validateEmailUseCase(_uiState.value.email)
        val passwordValidation = validatePasswordUseCase(_uiState.value.password)

        _uiState.update {
            it.copy(
                isFormValid = emailValidation.successful && passwordValidation.successful,
                emailError = emailValidation.errorMessage,
                passwordError = passwordValidation.errorMessage,
            )
        }
    }

    private fun signIn() {
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = signInUseCase(
                _uiState.value.email,
                _uiState.value.password
            )

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Неизвестная ошибка",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    fun startGoogleSignIn(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val signInIntent = startGoogleSignInUseCase(activity)
            _navigationEvent.emit(LoginNavigationEvent.LaunchGoogleSignIn(signInIntent))
        }
    }

    fun handleGoogleSignInResult(resultCode: Int, data: Intent?) {
        viewModelScope.launch {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    val idToken = signInWithGoogleUseCase.handleSignInResult(data)
                    idToken.fold(
                        onSuccess = { token ->
                            val authResult = signInWithGoogleUseCase.signInWithToken(token)
                            authResult.fold(
                                onSuccess = {
                                    _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                                },
                                onFailure = { error ->
                                    _uiState.update {
                                        it.copy(
                                            error = error.message ?: "Ошибка аутентификации",
                                            isLoading = false
                                        )
                                    }
                                }
                            )
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    error = error.message ?: "Ошибка Google Sign-In",
                                    isLoading = false
                                )
                            }
                        }
                    )
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Google Sign-In отменен",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Ошибка: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}