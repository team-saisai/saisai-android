package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseDetailDto(
    val courseId: String,
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String?,
    val inProgressUserCount: Int,
    val completeUserCount: Int,
    @SerialName("gpxPoints")
    val gpxPointDtoList: List<GpxPointDto> = emptyList(),
)
