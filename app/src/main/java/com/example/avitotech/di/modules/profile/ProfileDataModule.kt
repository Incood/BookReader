package com.example.avitotech.di.modules.profile

import com.example.avitotech.data.datasource.FirebaseProfileDataSource
import com.example.avitotech.data.datasource.ProfileDataSource
import com.example.avitotech.data.datasource.remote.YandexCloudDataSource
import com.example.avitotech.data.repository.ProfileRepositoryImpl
import com.example.avitotech.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideProfileDataSource(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        yandexCloudDataSource: YandexCloudDataSource
    ): ProfileDataSource = FirebaseProfileDataSource(firebaseAuth, firestore, yandexCloudDataSource)

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileDataSource: ProfileDataSource
    ): ProfileRepository = ProfileRepositoryImpl(profileDataSource)
}