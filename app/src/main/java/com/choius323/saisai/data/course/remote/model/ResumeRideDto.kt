package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ResumeRideDto(
    val rideId: Long,
    val durationSecond: Long,
    val checkpointIdx: Int,
)