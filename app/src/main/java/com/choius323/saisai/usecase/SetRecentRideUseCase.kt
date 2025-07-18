package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.RecentRide

class SetRecentRideUseCase(
    private val repository: CourseRepository,
) {
    suspend operator fun invoke(
        rideId: Long, courseId: Long, lastLatitude: Double, lastLongitude: Double,
        lastRideIndex: Int, isSendData: Boolean, progressRate: Double,
    ) {
        repository.setRecentRide(
            RecentRide(
                rideId = rideId,
                courseId = courseId,
                lastLatitude = lastLatitude,
                lastLongitude = lastLongitude,
                lastRideIndex = lastRideIndex,
                isSendData = isSendData,
                progressRate = progressRate
            )
        )
    }
}