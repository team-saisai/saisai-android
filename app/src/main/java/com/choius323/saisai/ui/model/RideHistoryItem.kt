package com.choius323.saisai.ui.model

import com.choius323.saisai.R
import org.threeten.bp.LocalDate

data class RideHistoryItem(
    val rideId: Long,
    val courseId: Long,
    val courseName: String,
    val sigun: String,
    val level: Level,
    val lastRideDate: LocalDate,
    val distance: Double,
    val duration: Long,
    val progressRate: Int,
    val imageUrl: Any? = null,
    val isCompleted: Boolean,
    val challengeStatus: String?,
    val challengeEndedAt: LocalDate?,
    val isEventActive: Boolean,
) {
    companion object {
        val sample1 = RideHistoryItem(
            rideId = 1,
            courseId = 215,
            courseName = "서해랑길 44코스",
            level = Level.Medium,
            distance = 14.0,
            sigun = "전북 부안군",
            imageUrl = R.drawable.img_course_ex,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.of(2026, 5, 13),
            isEventActive = true,
            lastRideDate = LocalDate.of(2020, 2, 3),
            duration = 10000,
            progressRate = 30,
            isCompleted = false,
        )
        val sample2 = RideHistoryItem(
            rideId = 2,
            courseId = 21533,
            courseName = "서서서서해랑길 44444-24코스",
            level = Level.High,
            distance = 32.0,
            sigun = "전북 부안군",
            imageUrl = R.drawable.img_course_ex,
            challengeStatus = ChallengeStatus.ENDED.name,
            challengeEndedAt = LocalDate.of(2026, 5, 13),
            isEventActive = false,
            lastRideDate = LocalDate.of(2020, 2, 3),
            duration = 5000,
            progressRate = 100,
            isCompleted = true,
        )
    }
}
