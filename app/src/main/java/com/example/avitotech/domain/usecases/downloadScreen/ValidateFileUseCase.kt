package com.example.avitotech.domain.usecases.downloadScreen

import android.net.Uri
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookUploadRepository

class ValidateFileUseCase(
    private val bookUploadRepository: BookUploadRepository
) {
    suspend operator fun invoke(fileUri: Uri): Resource<Unit> {
        return bookUploadRepository.validateBookFile(fileUri)
    }
}