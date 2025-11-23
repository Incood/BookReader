package com.example.avitotech.domain.usecases.auth

import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.AuthRepository
import com.example.avitotech.domain.repository.NetworkRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (!networkRepository.isNetworkAvailable()) {
            return Result.failure(Exception("Отсутствует подключение к интернету"))
        }
        return authRepository.signUpWithEmailAndPassword(email, password)
    }
}