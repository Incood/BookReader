package com.example.avitotech.domain.models

sealed class BookDownloadState {
    object Idle : BookDownloadState()
    object Downloading : BookDownloadState()
    data class Success(val filePath: String) : BookDownloadState()
    data class Error(val message: String) : BookDownloadState()
}