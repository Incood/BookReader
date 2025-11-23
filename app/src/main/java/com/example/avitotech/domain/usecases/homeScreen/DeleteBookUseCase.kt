package com.example.avitotech.domain.usecases.homeScreen

import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Resource<Unit> {
        return bookRepository.deleteLocalBook(bookId)
    }
}