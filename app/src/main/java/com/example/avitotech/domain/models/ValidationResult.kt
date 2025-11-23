package com.example.avitotech.domain.models

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)
