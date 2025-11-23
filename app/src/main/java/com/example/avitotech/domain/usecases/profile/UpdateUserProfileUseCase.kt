package com.example.avitotech.domain.usecases.profile

import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> = profileRepository.updateUserProfile(user)
}