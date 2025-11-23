package com.example.avitotech.domain.usecases.auth

import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? = authRepository.getCurrentUser()
}