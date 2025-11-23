package com.example.avitotech.domain.usecases.auth

import com.example.avitotech.domain.models.ValidationResult

class ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(
                successful = false,
                errorMessage = "Пароль не может быть пустым"
            )
            password.length < 6 -> ValidationResult(
                successful = false,
                errorMessage = "Пароль должен содержать минимум 6 символов"
            )
            else -> ValidationResult(successful = true)
        }
    }
}