package com.example.avitotech.data.datasource.local

import android.content.Context
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class LocalStorageDataSourceImpl @Inject constructor(
    private val context: Context
) : LocalStorageDataSource {

    private val booksDir: File by lazy {
        File(context.filesDir, "books").apply {
            if (!exists()) mkdirs()
        }
    }

    override suspend fun saveBook(inputStream: InputStream, book: Book): Resource<File> {
        return withContext(Dispatchers.IO) {
            try {
                val bookFile = File(booksDir, "${book.id}.${book.format}")
                inputStream.use { input ->
                    bookFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Resource.Success(bookFile)
            } catch (e: Exception) {
                Resource.Error("Failed to save book locally: ${e.message}")
            }
        }
    }

    override suspend fun getLocalBook(bookId: String): Resource<File> {
        return withContext(Dispatchers.IO) {
            try {
                val bookFile = booksDir.listFiles()?.find { it.nameWithoutExtension == bookId }
                if (bookFile != null && bookFile.exists()) {
                    Resource.Success(bookFile)
                } else {
                    Resource.Error("Book not found locally")
                }
            } catch (e: Exception) {
                Resource.Error("Failed to get local book: ${e.message}")
            }
        }
    }

    override suspend fun deleteLocalBook(bookId: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val bookFile = booksDir.listFiles()?.find { it.nameWithoutExtension == bookId }
                if (bookFile != null && bookFile.exists()) {
                    if (bookFile.delete()) {
                        Resource.Success(Unit)
                    } else {
                        Resource.Error("Failed to delete book file")
                    }
                } else {
                    Resource.Error("Book file not found")
                }
            } catch (e: Exception) {
                Resource.Error("Failed to delete local book: ${e.message}")
            }
        }
    }

    override suspend fun getLocalBooks(): List<Book> {
        return withContext(Dispatchers.IO) {
            try {
                booksDir.listFiles()?.mapNotNull { file ->
                    Book(
                        id = file.nameWithoutExtension,
                        title = file.nameWithoutExtension,
                        filename = file.name,
                        filePath = file.absolutePath,
                        isDownloaded = true
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun isBookDownloaded(bookId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val bookFile = booksDir.listFiles()?.find { it.nameWithoutExtension == bookId }
            bookFile != null && bookFile.exists()
        }
    }
}