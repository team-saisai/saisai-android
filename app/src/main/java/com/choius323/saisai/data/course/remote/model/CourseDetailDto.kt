package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseDetailDto(
    val courseId: Long,
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double, // km
    val estimatedTime: Double, // 분
    val sigun: String,
    val imageUrl: String? = null,
    val challengerCount: Int, // 챌린지 참여자 수
    val finisherCount: Int, // 완주 수
    val rideId: Long?,
    val challengeStatus: String?,
    val challengeEndedAt: String?,
    val isEventActive: Boolean?,
    @SerialName("gpxPoints")
    val gpxPointDtoList: List<GpxPointDto> = emptyList(),
    @SerialName("checkpoint")
    val checkpointList: List<PointDto> = emptyList(),
    val durationSecond: Long? = null,
    val checkpointIdx: Int? = null,
)
