package com.example.avitotech.presentation.screens.booksDownload.state

import android.net.Uri
import com.example.avitotech.domain.models.Resource

data class BookUploadState(
    val title: String = "",
    val author: String = "",
    val selectedFileUri: Uri? = null,
    val fileName: String = "",
    val uploadState: Resource<Unit>? = null,
    val validationState: Resource<Unit>? = null,
    val progress: Float = 0f,
    val isUploading: Boolean = false
) {
    val isLoading: Boolean get() = isUploading
    val uploadProgress: Float get() = progress
    val error: String? get() = (uploadState as? Resource.Error)?.message
    val isSuccess: Boolean get() = uploadState is Resource.Success
}