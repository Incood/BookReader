package com.example.avitotech.domain.models

sealed class BookDownloadState {
    object Idle : BookDownloadState()
    object Downloading : BookDownloadState()
    object Downloaded : BookDownloadState()
    object NotDownloaded : BookDownloadState()
    data class Error(val message: String) : BookDownloadState()
}