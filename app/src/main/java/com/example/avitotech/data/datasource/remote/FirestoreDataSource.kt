package com.example.avitotech.data.datasource.remote

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource

interface FirestoreDataSource {
    suspend fun getUserBooks(): List<Book>
    suspend fun saveBook(book: Book)
    suspend fun deleteBook(bookId: String): Resource<Unit>
}