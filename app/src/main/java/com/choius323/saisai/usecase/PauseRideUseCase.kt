package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class PauseRideUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        rideId: Long,
        duration: Long,
        checkPointIdx:Int,
    ): Flow<Result<Unit>> =
        courseRepository.pauseRide(rideId, duration, checkPointIdx)
}
