package com.choius323.saisai.ui.model


import org.threeten.bp.LocalDate

data class PopularChallengeListItem(
    val courseId: Long,
    val challengeStatus: String,
    val courseName: String,
    val distance: Double,
    val endedAt: LocalDate,
    val estimatedTime: Double,
    val level: Level,
    val participantCount: Int,
    val sigun: String,
    val isEventActive: Boolean,
    val reward: Int,
    val imageUrl: String? = null,
) {
    companion object {
        val dummyItem1 = PopularChallengeListItem(
            challengeStatus = "eruditi",
            courseName = "Camille Sheppard",
            distance = 6.7,
            endedAt = LocalDate.now().plusDays(1),
            estimatedTime = 5284.0,
            level = Level.Low,
            participantCount = 6935,
            sigun = "neque",
            imageUrl = "",
            courseId = 12,
            isEventActive = true,
            reward = 1000
        )
        val dummyItem2 =
            PopularChallengeListItem(
                challengeStatus = "scripta",
                courseName = "Alejandro Padilla",
                distance = 12.13,
                endedAt = LocalDate.now().minusDays(1),
                estimatedTime = 7293.0,
                level = Level.Medium,
                participantCount = 5304,
                sigun = "solum",
                imageUrl = "",
                courseId = 561,
                isEventActive = false,
                reward = 2000
            )
    }
}