package com.example.avitotech.data.datasource

import com.example.avitotech.data.datasource.remote.YandexCloudDataSource
import com.example.avitotech.data.extensions.toDomainUser
import com.example.avitotech.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseProfileDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val yandexCloudDataSource: YandexCloudDataSource
) : ProfileDataSource {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val PROFILE_IMAGES_FOLDER = "profile_images"
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            val firebaseUser = firebaseAuth.currentUser ?: return null

            val baseUser = firebaseUser.toDomainUser()

            val userDoc = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            if (userDoc.exists()) {
                val firestoreName = userDoc.getString("name")
                val firestoreProfilePicture = userDoc.getString("profilePicture")
                val firestorePhoneNumber = userDoc.getString("phoneNumber")

                baseUser.copy(
                    name = firestoreName ?: baseUser.name,
                    profilePicture = firestoreProfilePicture ?: baseUser.profilePicture,
                    phoneNumber = firestorePhoneNumber ?: baseUser.phoneNumber
                )
            } else {
                baseUser
            }
        } catch (e: Exception) {
            firebaseAuth.currentUser?.toDomainUser()
        }
    }

    override suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("Пользователь не авторизован"))

            val profileUpdates = UserProfileChangeRequest.Builder().apply {
                user.name?.let { setDisplayName(it) }
                user.profilePicture?.let { setPhotoUri(android.net.Uri.parse(it)) }
            }.build()

            firebaseUser.updateProfile(profileUpdates).await()

            val userData = hashMapOf(
                "name" to user.name,
                "profilePicture" to user.profilePicture,
                "phoneNumber" to user.phoneNumber,
                "email" to user.email,
                "updatedAt" to com.google.firebase.Timestamp.now()
            )

            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(userData)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка обновления профиля: ${e.message}"))
        }
    }

    override suspend fun uploadProfileImage(imageBytes: ByteArray): Result<String> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("Пользователь не авторизован"))

            val fileName = "$PROFILE_IMAGES_FOLDER/${userId}_${UUID.randomUUID()}.jpg"
            val uploadResult = yandexCloudDataSource.uploadFile(
                inputStream = imageBytes.inputStream(),
                remotePath = fileName,
                contentType = "image/jpeg",
                contentLength = imageBytes.size.toLong()
            )

            when (uploadResult) {
                is com.example.avitotech.domain.models.Resource.Success -> {
                    val urlResult = yandexCloudDataSource.getPresignedUrl(fileName)
                    when (urlResult) {
                        is com.example.avitotech.domain.models.Resource.Success -> {
                            Result.success(urlResult.data)
                        }
                        is com.example.avitotech.domain.models.Resource.Error -> {
                            Result.failure(Exception("Ошибка получения URL: ${urlResult.message}"))
                        }
                        else -> Result.failure(Exception("Неизвестная ошибка получения URL"))
                    }
                }
                is com.example.avitotech.domain.models.Resource.Error -> {
                    Result.failure(Exception("Ошибка загрузки: ${uploadResult.message}"))
                }
                else -> Result.failure(Exception("Неизвестная ошибка загрузки"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка загрузки изображения: ${e.message}"))
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка выхода: ${e.message}"))
        }
    }

    override fun getAuthState(): Flow<Boolean> = callbackFlow {
        trySend(firebaseAuth.currentUser != null)
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }
}