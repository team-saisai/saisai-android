package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseItemDto(
    val courseId: Long,
    val courseName: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String? = null,
    val courseChallengerCount: Int,
    val courseFinisherCount: Int,
    val challengeStatus: String,
    val challengeEndedAt: String,
    val isEventActive: Boolean,
    val reward: Int,
)
