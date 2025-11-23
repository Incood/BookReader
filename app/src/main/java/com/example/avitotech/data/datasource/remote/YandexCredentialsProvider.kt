package com.example.avitotech.data.datasource.remote

import com.amazonaws.auth.AWSCredentials

interface YandexCredentialsProvider {
    fun getCredentials(): AWSCredentials
    fun getBucketName(): String
    fun getUserId(): String
}