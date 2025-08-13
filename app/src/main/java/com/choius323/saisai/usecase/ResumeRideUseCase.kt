package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.ResumeRideInfo
import kotlinx.coroutines.flow.Flow

class ResumeRideUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(rideId: Long): Flow<Result<ResumeRideInfo>> =
        courseRepository.resumeRide(rideId)
}