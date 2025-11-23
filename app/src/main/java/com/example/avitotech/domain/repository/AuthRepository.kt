package com.example.avitotech.domain.repository

import android.app.Activity
import android.content.Intent
import com.example.avitotech.domain.models.User

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String) : Result<User>
    suspend fun signInWithGoogle(idToken: String) : Result<User>
    suspend fun signUpWithEmailAndPassword(email: String, password: String) : Result<User>
    fun getCurrentUser(): User?
    suspend fun signOut(): Result<Unit>
    fun isUserAuthenticated(): Boolean
    fun getGoogleSignInIntent(activity: Activity): Intent
    suspend fun handleGoogleSignInResult(data: Intent?): Result<String>
}