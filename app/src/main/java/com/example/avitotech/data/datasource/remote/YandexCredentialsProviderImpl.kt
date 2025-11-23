package com.example.avitotech.data.datasource.remote

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.example.avitotech.domain.repository.AuthRepository
import com.example.avitotech.domain.repository.SecureStorage
import javax.inject.Inject

class YandexCredentialsProviderImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val secureStorage: SecureStorage
) : YandexCredentialsProvider {

    override fun getCredentials(): AWSCredentials {
        val accessKey = secureStorage.getAccessKey()
        val secretKey = secureStorage.getSecretKey()
        return BasicAWSCredentials(accessKey, secretKey)
    }

    override fun getBucketName(): String = "avitotech-books"

    override fun getUserId(): String {
        return authRepository.getCurrentUser()?.id ?: throw IllegalStateException("User not authenticated")
    }
}