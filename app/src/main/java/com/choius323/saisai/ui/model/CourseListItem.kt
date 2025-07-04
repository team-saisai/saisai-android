package com.choius323.saisai.ui.model

data class CourseListItem(
    val courseId: Long,
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String?,
    val challengeInfo: ChallengeInfo?,
)