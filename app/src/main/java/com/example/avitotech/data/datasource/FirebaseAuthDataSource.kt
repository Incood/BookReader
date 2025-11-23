package com.example.avitotech.data.datasource

import android.app.Activity
import android.content.Intent
import com.example.avitotech.R
import com.example.avitotech.data.extensions.toDomainUser
import com.example.avitotech.domain.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.toDomainUser()?.let { user ->
                Result.success(user)
            } ?: Result.failure(Exception("Ошибка аутентификации"))
        } catch (e: Exception) {
            Result.failure(handleFirebaseError(e))
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.toDomainUser()?.let { user ->
                Result.success(user)
            } ?: Result.failure(Exception("Ошибка аутентификации Google"))
        } catch (e: Exception) {
            Result.failure(handleFirebaseError(e))
        }
    }

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.toDomainUser()?.let { user ->
                Result.success(user)
            } ?: Result.failure(Exception("Ошибка регистрации"))
        } catch (e: Exception) {
            Result.failure(handleFirebaseError(e))
        }
    }

    suspend fun signOut(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getGoogleSignInIntent(activity: Activity): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        return googleSignInClient.signInIntent
    }

    suspend fun handleGoogleSignInResult(data: Intent?): Result<String> {
        return try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            val idToken = account.idToken
            if (idToken != null) {
                Result.success(idToken)
            } else {
                Result.failure(Exception("Не удалось получить токен от Google"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка Google Sign-In: ${e.message}"))
        }
    }

    private fun handleFirebaseError(exception: Exception): Exception {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException ->
                Exception("Неверный email или пароль")
            is FirebaseAuthInvalidUserException ->
                Exception("Пользователь не найден")
            is FirebaseAuthUserCollisionException ->
                Exception("Пользователь с таким email уже существует")
            is FirebaseNetworkException ->
                Exception("Ошибка сети. Проверьте подключение к интернету")
            else -> Exception("Ошибка аутентификации: ${exception.message}")
        }
    }
}