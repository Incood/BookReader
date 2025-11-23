package com.example.avitotech.presentation.screens.booksDownload.state

import android.net.Uri

sealed class BookUploadEvent {
    data class TitleChanged(val title: String) : BookUploadEvent()
    data class AuthorChanged(val author: String) : BookUploadEvent()
    data class FileSelected(val fileUri: Uri, val fileName: String = "") : BookUploadEvent()
    object UploadBook : BookUploadEvent()
    object ValidateFile : BookUploadEvent()
    object ResetState : BookUploadEvent()
}