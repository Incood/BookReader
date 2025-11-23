package com.example.avitotech.di.modules.books

import android.content.Context
import com.example.avitotech.data.datasource.local.LocalStorageDataSource
import com.example.avitotech.data.datasource.local.LocalStorageDataSourceImpl
import com.example.avitotech.data.datasource.remote.FirestoreDataSource
import com.example.avitotech.data.datasource.remote.FirestoreDataSourceImpl
import com.example.avitotech.data.datasource.remote.YandexCloudDataSource
import com.example.avitotech.data.datasource.remote.YandexCloudDataSourceImpl
import com.example.avitotech.data.datasource.remote.YandexCredentialsProvider
import com.example.avitotech.data.repository.BookRepositoryImpl
import com.example.avitotech.data.repository.BookUploadRepositoryImpl
import com.example.avitotech.domain.repository.BookRepository
import com.example.avitotech.domain.repository.BookUploadRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BooksDataModule {

    @Provides
    @Singleton
    fun provideFirestoreDataSource(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirestoreDataSource = FirestoreDataSourceImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideYandexCloudDataSource(
        credentialsProvider: YandexCredentialsProvider
    ): YandexCloudDataSource = YandexCloudDataSourceImpl(credentialsProvider)

    @Provides
    @Singleton
    fun provideLocalStorageDataSource(
        @ApplicationContext context: Context
    ): LocalStorageDataSource = LocalStorageDataSourceImpl(context)

    @Provides
    @Singleton
    fun provideBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository = bookRepositoryImpl

    @Provides
    @Singleton
    fun provideBookUploadRepository(
        bookUploadRepositoryImpl: BookUploadRepositoryImpl
    ): BookUploadRepository = bookUploadRepositoryImpl
}