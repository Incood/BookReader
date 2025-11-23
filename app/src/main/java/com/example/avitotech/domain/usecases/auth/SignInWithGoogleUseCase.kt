package com.example.avitotech.domain.usecases.auth

import android.content.Intent
import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.AuthRepository
import com.example.avitotech.domain.repository.NetworkRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkRepository: NetworkRepository
) {
    suspend fun handleSignInResult(data: Intent?): Result<String> {
        if (!networkRepository.isNetworkAvailable()) {
            return Result.failure(Exception("Отсутствует подключение к интернету"))
        }
        return authRepository.handleGoogleSignInResult(data)
    }

    suspend fun signInWithToken(idToken: String): Result<User> {
        if (!networkRepository.isNetworkAvailable()) {
            return Result.failure(Exception("Отсутствует подключение к интернету"))
        }
        return authRepository.signInWithGoogle(idToken)
    }
}