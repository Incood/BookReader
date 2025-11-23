package com.example.avitotech.domain.repository

import android.net.Uri
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface BookUploadRepository {
    suspend fun uploadBook(book: Book, fileUri: Uri): Flow<Resource<Unit>>
    suspend fun validateBookFile(fileUri: Uri): Resource<Unit>
    suspend fun getSupportedFormats(): List<String>
}