package com.example.avitotech.data.repository

import com.example.avitotech.data.datasource.ProfileDataSource
import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {

    override suspend fun getCurrentUser(): User? = profileDataSource.getCurrentUser()

    override suspend fun updateUserProfile(user: User): Result<Unit> =
        profileDataSource.updateUserProfile(user)

    override suspend fun uploadProfileImage(imageBytes: ByteArray): Result<String> =
        profileDataSource.uploadProfileImage(imageBytes)

    override suspend fun signOut(): Result<Unit> = profileDataSource.signOut()

    override fun getAuthState(): Flow<Boolean> = profileDataSource.getAuthState()
}