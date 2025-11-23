package com.example.avitotech.domain.usecases.homeScreen

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookRepository

class GetBooksUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): Resource<List<Book>> {
        return bookRepository.getBooks()
    }
}