package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class CourseListItem(
    val courseId: Long,
    val courseName: String,
    val level: Level,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String?,
    val participantsCount: Int,
    val isBookmarked: Boolean,
    val challengeStatus: String?,
    val challengeEndedAt: LocalDate?,
    val isEventActive: Boolean,
    val reward: Int,
) {
    companion object {
        val dummyItem1 = CourseListItem(
            courseId = 123,
            courseName = "한강 라이딩 코스",
            level = Level.from(2),
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "서울",
            imageUrl = "https://example.com/image.jpg",
            participantsCount = 9982,
            challengeStatus = "ENDED",
            challengeEndedAt = LocalDate.of(2025, 11, 20),
            isEventActive = true,
            isBookmarked = false,
            reward = 1000,
        )
        val dummyItem2 = CourseListItem(
            courseId = 813,
            courseName = "한강 라이딩 코스",
            level = Level.from(1),
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "서울",
            imageUrl = "https://example.com/image.jpg",
            participantsCount = 982,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.now().plusDays(33),
            isEventActive = false,
            isBookmarked = true,
            reward = 0,
        )
        val dummyItem3 = CourseListItem(
            courseId = 13,
            courseName = "해운대 코스",
            level = Level.from(3),
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "부산",
            imageUrl = "https://example.com/image.jpg",
            participantsCount = 982,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.now().plusDays(7),
            isEventActive = true,
            isBookmarked = true,
            reward = 500,
        )
        val dummyItem4 = CourseListItem(
            courseId = 234,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "여의도 - 반포",
            level = Level.from(2),
            distance = 8.6,
            estimatedTime = 50.0,
            sigun = "서울시 강남구",
            participantsCount = 282,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.now().plusDays(10),
            isEventActive = false,
            isBookmarked = false,
            reward = 0,
        )
    }
}