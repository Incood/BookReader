package com.example.avitotech.domain.usecases.readerScreen

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookRepository

class GetBookUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Resource<Book> {
        return bookRepository.getBookById(bookId)
    }
}