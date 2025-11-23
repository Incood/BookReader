package com.example.avitotech.domain.usecases.auth

import android.util.Patterns
import com.example.avitotech.domain.models.ValidationResult

class ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationResult {
        return if (email.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Email не может быть пустым"
            )
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidationResult(
                successful = false,
                errorMessage = "Некорректный формат email"
            )
        } else {
            ValidationResult(successful = true)
        }
    }
}