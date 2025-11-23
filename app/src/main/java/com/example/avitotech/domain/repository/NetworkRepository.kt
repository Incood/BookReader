package com.example.avitotech.domain.repository

interface NetworkRepository {

    suspend fun isNetworkAvailable(): Boolean
}