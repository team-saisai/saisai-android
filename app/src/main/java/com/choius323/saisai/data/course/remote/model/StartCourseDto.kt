package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartCourseDto(
    val rideId: Long,
    val sigun: String,
    val courseName: String,
    val distance: Double,
    @SerialName("gpxPoints")
    val gpxPointList: List<GpxPointDto> = emptyList(),
    @SerialName("checkpoints")
    val checkpointList: List<CheckPointDto> = emptyList(),
)