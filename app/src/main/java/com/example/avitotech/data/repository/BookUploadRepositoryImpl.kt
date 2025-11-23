package com.example.avitotech.data.repository

import android.content.Context
import android.net.Uri
import com.example.avitotech.data.datasource.local.LocalStorageDataSource
import com.example.avitotech.data.datasource.remote.FirestoreDataSource
import com.example.avitotech.data.datasource.remote.YandexCloudDataSource
import com.example.avitotech.data.datasource.remote.YandexCredentialsProvider
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.example.avitotech.domain.repository.BookUploadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class BookUploadRepositoryImpl @Inject constructor(
    private val yandexCloudDataSource: YandexCloudDataSource,
    private val localStorageDataSource: LocalStorageDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val credentialsProvider: YandexCredentialsProvider,
    @ApplicationContext private val context: Context
) : BookUploadRepository {

    companion object {
        private val SUPPORTED_FORMATS = listOf("txt", "epub", "pdf")
        private const val MAX_FILE_SIZE = 50 * 1024 * 1024
    }

    override suspend fun uploadBook(book: Book, fileUri: Uri): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading)
            validateBookFile(fileUri).let { validationResult ->
                if (validationResult is Resource.Error) {
                    emit(validationResult)
                    return@flow
                }
            }

            val userId = credentialsProvider.getUserId()
            val fileExtension = getFileExtension(fileUri)
            val filename = "${UUID.randomUUID()}.$fileExtension"
            val remotePath = "books/$userId/$filename"

            val inputStream = context.contentResolver.openInputStream(fileUri)
                ?: throw Exception("Cannot open file")
            val fileSize = getFileSize(fileUri)

            emit(Resource.Loading)
            yandexCloudDataSource.uploadFile(
                inputStream = inputStream,
                remotePath = remotePath,
                contentType = getMimeType(fileUri),
                contentLength = fileSize
            ).let { uploadResult ->
                if (uploadResult is Resource.Error) {
                    emit(uploadResult)
                    return@flow
                }
            }

            emit(Resource.Loading)
            val fileUrl = when (val urlResult = yandexCloudDataSource.getPresignedUrl(remotePath)) {
                is Resource.Success -> urlResult.data
                is Resource.Error -> {
                    emit(urlResult)
                    return@flow
                }
                else -> {
                    emit(Resource.Error("Failed to generate download URL"))
                    return@flow
                }
            }

            val bookWithMetadata = book.copy(
                id = UUID.randomUUID().toString(),
                filename = filename,
                fileUrl = fileUrl,
                userId = userId,
                format = fileExtension,
                isDownloaded = true
            )

            emit(Resource.Loading)
            firestoreDataSource.saveBook(bookWithMetadata)

            emit(Resource.Success(Unit))

        } catch (e: Exception) {
            emit(Resource.Error("Upload failed: ${e.message ?: "Unknown error"}"))
        }
    }

    override suspend fun validateBookFile(fileUri: Uri): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val fileExtension = getFileExtension(fileUri)
                if (fileExtension !in SUPPORTED_FORMATS) {
                    return@withContext Resource.Error(
                        "Unsupported file format. Supported formats: ${SUPPORTED_FORMATS.joinToString()}"
                    )
                }

                val fileSize = getFileSize(fileUri)
                if (fileSize > MAX_FILE_SIZE) {
                    return@withContext Resource.Error("File size exceeds maximum limit of 50MB")
                }

                val inputStream = context.contentResolver.openInputStream(fileUri)
                if (inputStream == null) {
                    return@withContext Resource.Error("Cannot access file")
                }
                inputStream.close()

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error("File validation failed: ${e.message}")
            }
        }
    }

    override suspend fun getSupportedFormats(): List<String> {
        return SUPPORTED_FORMATS
    }

    private fun getFileExtension(fileUri: Uri): String {
        val fileName = context.contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    cursor.getString(displayNameIndex)
                } else {
                    null
                }
            } else {
                null
            }
        }

        return fileName?.substringAfterLast('.', "")?.lowercase()
            ?: fileUri.toString().substringAfterLast('.').lowercase()
    }

    private fun getFileSize(fileUri: Uri): Long {
        return context.contentResolver.openFileDescriptor(fileUri, "r")?.use { parcelFileDescriptor ->
            parcelFileDescriptor.statSize
        } ?: 0L
    }

    private fun getMimeType(fileUri: Uri): String {
        return context.contentResolver.getType(fileUri) ?: when (getFileExtension(fileUri)) {
            "txt" -> "text/plain"
            "epub" -> "application/epub+zip"
            "pdf" -> "application/pdf"
            else -> "application/octet-stream"
        }
    }
}