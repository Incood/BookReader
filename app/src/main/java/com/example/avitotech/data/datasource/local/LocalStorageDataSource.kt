package com.example.avitotech.data.datasource.local

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import java.io.File
import java.io.InputStream

interface LocalStorageDataSource {
    suspend fun saveBook(inputStream: InputStream, book: Book): Resource<File>
    suspend fun getLocalBook(bookId: String): Resource<File>
    suspend fun deleteLocalBook(bookId: String): Resource<Unit>
    suspend fun getLocalBooks(): List<Book>
    suspend fun isBookDownloaded(bookId: String): Boolean
}