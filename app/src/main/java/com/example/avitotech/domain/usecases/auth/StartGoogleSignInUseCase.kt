package com.example.avitotech.domain.usecases.auth

import android.app.Activity
import android.content.Intent
import com.example.avitotech.domain.repository.AuthRepository
import javax.inject.Inject

class StartGoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(activity: Activity): Intent =
        authRepository.getGoogleSignInIntent(activity)
}