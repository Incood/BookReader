package com.example.avitotech.di.modules.profile

import com.example.avitotech.domain.repository.ProfileRepository
import com.example.avitotech.domain.usecases.profile.GetCurrentUserUseCase
import com.example.avitotech.domain.usecases.profile.SignOutUseCase
import com.example.avitotech.domain.usecases.profile.UpdateUserProfileUseCase
import com.example.avitotech.domain.usecases.profile.UploadProfileImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ProfileUseCasesModule {

    @Provides
    fun provideGetCurrentUserUseCase(
        profileRepository: ProfileRepository
    ): GetCurrentUserUseCase = GetCurrentUserUseCase(profileRepository)

    @Provides
    fun provideUpdateUserProfileUseCase(
        profileRepository: ProfileRepository
    ): UpdateUserProfileUseCase = UpdateUserProfileUseCase(profileRepository)

    @Provides
    fun provideUploadProfileImageUseCase(
        profileRepository: ProfileRepository
    ): UploadProfileImageUseCase = UploadProfileImageUseCase(profileRepository)

    @Provides
    fun provideSignOutUseCase(
        profileRepository: ProfileRepository
    ): SignOutUseCase = SignOutUseCase(profileRepository)
}