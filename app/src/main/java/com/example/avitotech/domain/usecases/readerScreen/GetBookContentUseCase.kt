package com.example.avitotech.domain.usecases.readerScreen

import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookRepository

class GetBookContentUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Resource<String> {
        return bookRepository.getBookContent(bookId)
    }
}