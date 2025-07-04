package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseDetailDto(
    val courseId: Long,
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String?,
    val challengerCount: Int, // 챌린지 참여자 수
    val finisherCount: Int, // 완주 수
    @SerialName("gpxPoints")
    val gpxPointDtoList: List<GpxPointDto> = emptyList(),
)
