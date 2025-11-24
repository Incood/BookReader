package com.example.avitotech.data.datasource.remote

import com.example.avitotech.data.extensions.toDomainUser
import com.example.avitotech.domain.models.Book
import com.example.avitotech.domain.models.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirestoreDataSource {

    companion object {
        private const val BOOKS_COLLECTION = "books"
    }

    override suspend fun getUserBooks(): List<Book> {
        return try {
            val snapshot = firestore.collection(BOOKS_COLLECTION)
                .whereEqualTo("userId", getCurrentUserId())
                .get()
                .await()

            snapshot.documents.map { document ->
                document.toObject<Book>()?.copy(id = document.id) ?: throw IllegalStateException("Invalid book data")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveBook(book: Book) {
        try {
            val bookData = mapOf(
                "title" to book.title,
                "author" to book.author,
                "fileUrl" to book.fileUrl,
                "userId" to book.userId,
                "filename" to book.filename,
                "format" to book.format
            )

            if (book.id.isNotEmpty()) {
                firestore.collection(BOOKS_COLLECTION)
                    .document(book.id)
                    .set(bookData)
                    .await()
            } else {
                firestore.collection(BOOKS_COLLECTION)
                    .add(bookData)
                    .await()
            }
        } catch (e: Exception) {
            throw Exception("Failed to save book: ${e.message}")
        }
    }

    override suspend fun deleteBook(bookId: String): Resource<Unit> {
        return try {
            firestore.collection(BOOKS_COLLECTION)
                .document(bookId)
                .delete()
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to delete book: ${e.message}")
        }
    }

    private fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.toDomainUser()?.id ?: throw IllegalStateException("User not authenticated")
    }
}