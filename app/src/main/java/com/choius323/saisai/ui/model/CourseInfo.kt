package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class CourseInfo(
    val courseId: Int,
    val imageUrl: String,
    val endDate: LocalDate,
    val startPlace: String,
    val endPlace: String,
    val distance: String,
    val difficulty: String,
    val challengerCount: Int,
    val completedCount: Int = 0,
) {
    companion object {
        val dummyItem = CourseInfo(
            courseId = 1,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            endDate = LocalDate.of(2025, 7, 18),
            distance = "8.6km",
            difficulty = "하",
            challengerCount = 207,
            completedCount = 412,
            startPlace = "여의대로",
            endPlace = "국회대로",
        )
    }
}