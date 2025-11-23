package com.example.avitotech.di

import com.example.avitotech.data.datasource.ProfileDataSource
import com.example.avitotech.di.modules.auth.AuthModule
import com.example.avitotech.di.modules.auth.AuthUseCasesModule
import com.example.avitotech.di.modules.books.BooksDataModule
import com.example.avitotech.di.modules.books.BooksUseCasesModule
import com.example.avitotech.di.modules.network.CoreModule
import com.example.avitotech.di.modules.network.NetworkModule
import com.example.avitotech.di.modules.profile.ProfileUseCasesModule
import com.example.avitotech.di.modules.reader.ReaderDataModule
import com.example.avitotech.di.modules.reader.ReaderUseCasesModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        AuthModule::class,
        AuthUseCasesModule::class,
        BooksDataModule::class,
        BooksUseCasesModule::class,
        ReaderDataModule::class,
        ReaderUseCasesModule::class,
        NetworkModule::class,
        CoreModule::class,
        ProfileUseCasesModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule