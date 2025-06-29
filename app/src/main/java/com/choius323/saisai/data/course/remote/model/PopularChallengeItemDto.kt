package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PopularChallengeItemDto(
    val challengeStatus: String,
    val courseName: String,
    val distance: Double,
    val endedAt: String,
    val estimatedTime: Int,
    val level: Int,
    val participantCount: Int,
    val sigun: String,
    val imageUrl: String? = null,
)