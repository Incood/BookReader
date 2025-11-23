package com.example.avitotech.di.modules.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.avitotech.data.datasource.remote.YandexCredentialsProvider
import com.example.avitotech.data.datasource.remote.YandexCredentialsProviderImpl
import com.example.avitotech.data.repository.NetworkRepositoryImpl
import com.example.avitotech.data.repository.SecureStorageImpl
import com.example.avitotech.domain.repository.NetworkRepository
import com.example.avitotech.domain.repository.SecureStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        networkRepositoryImpl: NetworkRepositoryImpl
    ): NetworkRepository

    @Binds
    @Singleton
    abstract fun bindYandexCredentialsProvider(
        yandexCredentialsProviderImpl: YandexCredentialsProviderImpl
    ): YandexCredentialsProvider
}

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideSecureStorage(
        @ApplicationContext context: Context
    ): SecureStorage = SecureStorageImpl(context)
}