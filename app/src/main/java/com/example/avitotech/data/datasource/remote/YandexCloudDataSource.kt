package com.example.avitotech.data.datasource.remote

import com.example.avitotech.domain.models.Resource
import java.io.InputStream

interface YandexCloudDataSource {
    suspend fun uploadFile(
        inputStream: InputStream,
        remotePath: String,
        contentType: String,
        contentLength: Long
    ): Resource<Unit>

    suspend fun getPresignedUrl(remotePath: String): Resource<String>
    suspend fun downloadFile(remotePath: String): Resource<InputStream>
}