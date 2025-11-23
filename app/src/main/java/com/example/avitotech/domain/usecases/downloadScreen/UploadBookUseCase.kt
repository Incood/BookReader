package com.example.avitotech.domain.usecases.downloadScreen

import android.net.Uri
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookUploadRepository
import kotlinx.coroutines.flow.Flow

class UploadBookUseCase(
    private val bookUploadRepository: BookUploadRepository
) {
    suspend operator fun invoke(book: Book, fileUri: Uri): Flow<Resource<Unit>> {
        return bookUploadRepository.uploadBook(book, fileUri)
    }
}