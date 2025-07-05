package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class RecentCourse(
    val courseId: Long,
    val courseName: String,
    val distance: Double,
    val sigun: String,
    val progressRate: Double,
    val recentDateAt: LocalDate,
    val imageUrl: String?,
)
