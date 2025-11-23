package com.example.avitotech.domain.repository

interface SecureStorage {
    fun getAccessKey(): String
    fun getSecretKey(): String
    fun saveAccessKey(accessKey: String)
    fun saveSecretKey(secretKey: String)
}