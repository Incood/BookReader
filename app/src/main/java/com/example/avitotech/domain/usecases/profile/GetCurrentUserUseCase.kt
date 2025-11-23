package com.example.avitotech.domain.usecases.profile

import com.example.avitotech.domain.models.User
import com.example.avitotech.domain.repository.ProfileRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): User? = profileRepository.getCurrentUser()
}