package com.example.avitotech.domain.repository

import com.example.avitotech.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getCurrentUser(): User?
    suspend fun updateUserProfile(user: User): Result<Unit>
    suspend fun uploadProfileImage(imageBytes: ByteArray): Result<String>
    suspend fun signOut(): Result<Unit>
    fun getAuthState(): Flow<Boolean>
}