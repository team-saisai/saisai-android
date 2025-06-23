package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseItemDto(
    val courseId: String = "EMPTY",
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String?,
    val challengeInfo: ChallengeInfoDto?,
)
