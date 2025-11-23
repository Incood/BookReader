package com.example.avitotech.data.repository

import android.app.Activity
import android.content.Intent
import com.example.avitotech.data.datasource.FirebaseAuthDataSource
import com.example.avitotech.data.extensions.toDomainUser
import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataSource: FirebaseAuthDataSource
): AuthRepository {

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> =
        dataSource.signInWithEmailAndPassword(email, password)

    override suspend fun signInWithGoogle(idToken: String): Result<User> =
        dataSource.signInWithGoogle(idToken)

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<User> =
        dataSource.signUpWithEmailAndPassword(email, password)

    override fun getCurrentUser(): User? = firebaseAuth.currentUser?.toDomainUser()

    override suspend fun signOut(): Result<Unit> = dataSource.signOut()

    override fun isUserAuthenticated(): Boolean = firebaseAuth.currentUser != null

    override fun getGoogleSignInIntent(activity: Activity): Intent =
        dataSource.getGoogleSignInIntent(activity)

    override suspend fun handleGoogleSignInResult(data: Intent?): Result<String> =
        dataSource.handleGoogleSignInResult(data)
}