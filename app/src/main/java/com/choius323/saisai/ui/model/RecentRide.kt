package com.choius323.saisai.ui.model

data class RecentRide(
    val rideId: Long,
    val courseId: Long,
    val lastLatitude: Double,
    val lastLongitude: Double,
    val lastRideIndex: Int,
    val isSendData: Boolean,
    val progressRate: Double,
)