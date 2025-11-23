package com.example.avitotech.domain.models

data class Book(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val filename: String = "",
    val fileUrl: String = "",
    val userId: String = "",
    val format: String = "",
    val isDownloaded: Boolean = false,
    val coverUrl: String = "",
    val filePath: String? = null,
    val downloadDate: Long = 0
)