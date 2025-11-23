package com.example.avitotech.domain.usecases.homeScreen

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookRepository
import java.io.File

class DownloadBookUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(book: Book): Resource<File> {
        return bookRepository.downloadBook(book)
    }
}