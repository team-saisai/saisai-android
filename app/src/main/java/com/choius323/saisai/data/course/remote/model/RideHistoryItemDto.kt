package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RideHistoryItemDto(
    val rideId:Long,
    val courseId: Long,
    val courseName: String,
    val sigun: String,
    val level: Int,
    val lastRideDate: String,
    val distance: Double,
    val durationSecond: Long,
    val progressRate:Int,
    val imageUrl: String? = null,
    val isCompleted: Boolean,
    val isBookmarked: Boolean = false,
    val challengeStatus: String?,
    val challengeEndedAt: String?,
    val isEventActive: Boolean,
)
