package com.example.avitotech.domain.usecases.homeScreen

import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.SortType

class SortBooksUseCase {

    operator fun invoke(books: List<Book>, sortType: SortType): List<Book> {
        return when (sortType) {
            SortType.TITLE  -> books.sortedBy { it.title }
            SortType.DATE   -> books.sortedByDescending { it.downloadDate }
        }
    }
}