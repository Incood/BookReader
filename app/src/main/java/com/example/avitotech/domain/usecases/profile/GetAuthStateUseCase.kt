package com.example.avitotech.domain.usecases.profile

import com.example.avitotech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Boolean> = profileRepository.getAuthState()
}