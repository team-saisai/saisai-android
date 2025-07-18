package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository

class GetRecentRideUseCase(
    private val repository: CourseRepository,
) {
    suspend operator fun invoke() = repository.getRecentRide()
}