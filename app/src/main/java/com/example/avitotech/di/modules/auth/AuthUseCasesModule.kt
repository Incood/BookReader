package com.example.avitotech.di.modules.auth

import com.example.avitotech.domain.repository.AuthRepository
import com.example.avitotech.domain.repository.NetworkRepository
import com.example.avitotech.domain.usecases.auth.GetCurrentUserUseCase
import com.example.avitotech.domain.usecases.auth.SignInUseCase
import com.example.avitotech.domain.usecases.auth.SignInWithGoogleUseCase
import com.example.avitotech.domain.usecases.auth.SignUpUseCase
import com.example.avitotech.domain.usecases.auth.ValidateEmailUseCase
import com.example.avitotech.domain.usecases.auth.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AuthUseCasesModule {

    @Provides
    fun provideSignInUseCase(
        authRepository: AuthRepository,
        networkRepository: NetworkRepository
    ): SignInUseCase = SignInUseCase(authRepository, networkRepository)

    @Provides
    fun provideSignInWithGoogleUseCase(
        authRepository: AuthRepository,
        networkRepository: NetworkRepository
    ): SignInWithGoogleUseCase = SignInWithGoogleUseCase(authRepository, networkRepository)

    @Provides
    fun provideSignUpUseCase(
        authRepository: AuthRepository,
        networkRepository: NetworkRepository
    ): SignUpUseCase = SignUpUseCase(authRepository, networkRepository)

    @Provides
    fun provideGetCurrentUserUseCase(
        authRepository: AuthRepository
    ): GetCurrentUserUseCase = GetCurrentUserUseCase(authRepository)

    @Provides
    fun provideValidateEmailUseCase(): ValidateEmailUseCase = ValidateEmailUseCase()

    @Provides
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase = ValidatePasswordUseCase()
}