package com.choius323.saisai.ui.model

data class CourseDetails(
    val name: String,
    val distance: String,
    val difficulty: String,
    val themes: List<String>,
    val challengerCount: Int,
    val completedCount: Int,
)