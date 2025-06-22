package com.choius323.saisai.ui.model

data class CourseInfo(
    val courseId: Int,
    val imageUrl: String,
    val endDate: String,
    val startPlace: String,
    val endPlace: String,
    val distance: String,
    val difficulty: String,
    val challengerCount: Int,
    val themes: List<String> = emptyList(),
    val completedCount: Int = 0,
)