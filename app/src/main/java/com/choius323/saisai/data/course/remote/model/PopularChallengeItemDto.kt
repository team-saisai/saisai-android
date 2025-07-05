package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PopularChallengeItemDto(
    val courseId: Long,
    val challengeStatus: String,
    val courseName: String,
    val distance: Double,
    val endedAt: String,
    val estimatedTime: Double,
    val level: Int,
    val challengerCount: Int,
    val sigun: String,
    val imageUrl: String? = null,
)