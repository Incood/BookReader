package com.example.avitotech.domain.usecases.profile

import com.example.avitotech.domain.repository.ProfileRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Unit> = profileRepository.signOut()
}