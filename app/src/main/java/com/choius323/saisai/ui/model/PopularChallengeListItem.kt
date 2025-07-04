package com.choius323.saisai.ui.model


import org.threeten.bp.LocalDateTime

data class PopularChallengeListItem(
    val courseId: Long,
    val challengeStatus: String,
    val courseName: String,
    val distance: Double,
    val endedAt: LocalDateTime,
    val estimatedTime: Int,
    val level: Int,
    val participantCount: Int,
    val sigun: String,
    val imageUrl: String? = null,
)