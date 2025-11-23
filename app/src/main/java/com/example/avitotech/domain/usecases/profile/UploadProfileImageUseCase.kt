package com.example.avitotech.domain.usecases.profile

import com.example.avitotech.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<String> =
        profileRepository.uploadProfileImage(imageBytes)
}