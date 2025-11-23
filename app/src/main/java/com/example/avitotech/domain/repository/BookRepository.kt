package com.example.avitotech.domain.repository

import android.net.Uri
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import java.io.File

interface BookRepository {
    suspend fun getBooks(): Resource<List<Book>>
    suspend fun uploadBook(book: Book, fileUri: Uri): Resource<Unit>
    suspend fun downloadBook(book: Book): Resource<File>
    suspend fun deleteLocalBook(bookId: String): Resource<Unit>
    suspend fun getBookContent(bookId: String): Resource<String>
    suspend fun getBookById(bookId: String): Resource<Book>
}