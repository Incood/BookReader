package com.example.avitotech.data.repository

import android.content.Context
import android.net.Uri
import com.example.avitotech.data.datasource.local.LocalStorageDataSource
import com.example.avitotech.data.datasource.remote.FirestoreDataSource
import com.example.avitotech.data.datasource.remote.YandexCloudDataSource
import com.example.avitotech.data.datasource.remote.YandexCredentialsProvider
import com.example.avitotech.data.utils.BookContentReader
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val yandexCloudDataSource: YandexCloudDataSource,
    private val localStorageDataSource: LocalStorageDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val credentialsProvider: YandexCredentialsProvider,
    @ApplicationContext private val context: Context
) : BookRepository {

    override suspend fun getBooks(): Resource<List<Book>> {
        return try {
            val remoteBooks = firestoreDataSource.getUserBooks()
            val syncedBooks = remoteBooks.map { remoteBook ->
                val isDownloaded = localStorageDataSource.isBookDownloaded(remoteBook.id)
                val localPath = if (isDownloaded) {
                    when (val localBookResult = localStorageDataSource.getLocalBook(remoteBook.id)) {
                        is Resource.Success -> localBookResult.data.absolutePath
                        else -> null
                    }
                } else {
                    null
                }

                remoteBook.copy(
                    isDownloaded = isDownloaded,
                    filePath = localPath
                )
            }

            Resource.Success(syncedBooks)
        } catch (e: Exception) {
            Resource.Error("Failed to load books: ${e.message}")
        }
    }

    override suspend fun uploadBook(book: Book, fileUri: Uri): Resource<Unit> {
        return try {
            val userId = credentialsProvider.getUserId()
            val remotePath = "books/$userId/${book.filename}"

            val inputStream = context.contentResolver.openInputStream(fileUri)
            val file = File(getFilePathFromUri(fileUri))

            yandexCloudDataSource.uploadFile(
                inputStream = inputStream!!,
                remotePath = remotePath,
                contentType = getMimeType(fileUri),
                contentLength = file.length()
            ).let { result ->
                if (result is Resource.Error) return result
            }

            val fileUrl = when (val urlResult = yandexCloudDataSource.getPresignedUrl(remotePath)) {
                is Resource.Success -> urlResult.data
                is Resource.Error -> return urlResult
                else -> return Resource.Error("Failed to generate URL")
            }

            val bookWithUrl = book.copy(
                fileUrl = fileUrl,
                userId = userId,
            )
            firestoreDataSource.saveBook(bookWithUrl)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Upload failed: ${e.message}")
        }
    }

    override suspend fun downloadBook(book: Book): Resource<File> {
        return try {
            val userId = credentialsProvider.getUserId()
            val remotePath = "books/$userId/${book.filename}"

            when (val result = yandexCloudDataSource.downloadFile(remotePath)) {
                is Resource.Success -> {
                    when (val saveResult = localStorageDataSource.saveBook(result.data, book)) {
                        is Resource.Success -> {
                            Resource.Success(saveResult.data)
                        }
                        is Resource.Error -> Resource.Error(saveResult.message)
                        else -> Resource.Error("Unknown error")
                    }
                }
                is Resource.Error -> Resource.Error(result.message)
                else -> Resource.Error("Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error("Download failed: ${e.message}")
        }
    }

    override suspend fun deleteLocalBook(bookId: String): Resource<Unit> {
        return try {
            localStorageDataSource.deleteLocalBook(bookId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to delete book: ${e.message}")
        }
    }

    override suspend fun getBookContent(bookId: String): Resource<String> {
        return try {
            val localBook = localStorageDataSource.getLocalBook(bookId)
            if (localBook is Resource.Success) {
                val bookContentReader = BookContentReader(context)
                bookContentReader.readBookContent(localBook.data)
            } else {
                Resource.Error("Книга не найдена локально")
            }
        } catch (e: Exception) {
            Resource.Error("Ошибка чтения книги: ${e.message}")
        }
    }

    override suspend fun getBookById(bookId: String): Resource<Book> {
        return try {
            val userBooksResult = getBooks()

            when (userBooksResult) {
                is Resource.Success -> {
                    val userBooks = userBooksResult.data
                    val book = userBooks.find { book -> book.id == bookId }

                    if (book != null) {
                        Resource.Success(book)
                    } else {
                        Resource.Error("Книга не найдена")
                    }
                }
                is Resource.Error -> {
                    Resource.Error(userBooksResult.message)
                }
                else -> {
                    Resource.Error("Не удалось загрузить список книг")
                }
            }
        } catch (e: Exception) {
            Resource.Error("Ошибка: ${e.message}")
        }
    }

    private fun getMimeType(uri: Uri): String {
        return context.contentResolver.getType(uri) ?: "application/octet-stream"
    }

    private fun getFilePathFromUri(uri: Uri): String {
        var filePath = ""
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow("_data")
                filePath = cursor.getString(columnIndex)
            }
        }
        return filePath
    }
}