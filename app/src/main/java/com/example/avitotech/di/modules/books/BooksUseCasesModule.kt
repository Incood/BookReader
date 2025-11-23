package com.example.avitotech.di.modules.books

import com.example.avitotech.domain.repository.BookRepository
import com.example.avitotech.domain.repository.BookUploadRepository
import com.example.avitotech.domain.usecases.downloadScreen.UploadBookUseCase
import com.example.avitotech.domain.usecases.downloadScreen.ValidateFileUseCase
import com.example.avitotech.domain.usecases.homeScreen.DeleteBookUseCase
import com.example.avitotech.domain.usecases.homeScreen.DownloadBookUseCase
import com.example.avitotech.domain.usecases.homeScreen.GetBooksUseCase
import com.example.avitotech.domain.usecases.homeScreen.SortBooksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BooksUseCasesModule {

    @Provides
    fun provideGetBooksUseCase(bookRepository: BookRepository): GetBooksUseCase =
        GetBooksUseCase(bookRepository)

    @Provides
    fun provideDownloadBookUseCase(bookRepository: BookRepository): DownloadBookUseCase =
        DownloadBookUseCase(bookRepository)

    @Provides
    fun provideDeleteBookUseCase(bookRepository: BookRepository): DeleteBookUseCase =
        DeleteBookUseCase(bookRepository)

    @Provides
    fun provideSortBooksUseCase(): SortBooksUseCase = SortBooksUseCase()

    @Provides
    fun provideUploadBookUseCase(
        bookUploadRepository: BookUploadRepository
    ): UploadBookUseCase = UploadBookUseCase(bookUploadRepository)

    @Provides
    fun provideValidateFileUseCase(
        bookUploadRepository: BookUploadRepository
    ): ValidateFileUseCase = ValidateFileUseCase(bookUploadRepository)
}