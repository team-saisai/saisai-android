package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class RideHistoryItem(
    val rideId: Long,
    val courseId: Long,
    val courseName: String,
    val sigun: String,
    val level: Level,
    val lastRideDate: LocalDate,
    val distance: Double,
    val estimatedTime: Double,
    val progressRate: Int,
    val imageUrl: String? = null,
    val isCompleted: Boolean,
    val challengeStatus: String?,
    val challengeEndedAt: LocalDate?,
    val isEventActive: Boolean,
)
