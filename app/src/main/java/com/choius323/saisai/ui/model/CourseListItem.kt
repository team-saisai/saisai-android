package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class CourseListItem(
    val courseId: Long,
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val imageUrl: String?,
    val courseChallengerCount: Int,
    val courseFinisherCount: Int,
    val challengeStatus: String,
    val challengeEndedAt: LocalDate,
) {
    companion object {
        val dummyItem1 = CourseListItem(
            courseId = 123,
            courseName = "한강 라이딩 코스",
            summary = "서울의 아름다운 풍경을 따라",
            level = 2,
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "서울",
            imageUrl = "https://example.com/image.jpg",
            courseChallengerCount = 4757,
            courseFinisherCount = 9982,
            challengeStatus = "ENDED",
            challengeEndedAt = LocalDate.now().minusDays(3),
        )
        val dummyItem2 = CourseListItem(
            courseId = 813,
            courseName = "한강 라이딩 코스",
            summary = "서울의 아름다운 풍경을 따라",
            level = 1,
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "서울",
            imageUrl = "https://example.com/image.jpg",
            courseChallengerCount = 57,
            courseFinisherCount = 982,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.now().plusDays(33),
        )
        val dummyItem3 = CourseListItem(
            courseId = 13,
            courseName = "해운대 코스",
            summary = "해운대의 아름다운 풍경을 따라",
            level = 3,
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "부산",
            imageUrl = "https://example.com/image.jpg",
            courseChallengerCount = 757,
            courseFinisherCount = 982,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.now().plusDays(7),
        )
        val dummyItem4 = CourseListItem(
            courseId = 234,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "여의도 - 반포",
            summary = "dapibus",
            level = 2,
            distance = 8.6,
            estimatedTime = 50.0,
            sigun = "서울시 강남구",
            courseChallengerCount = 57,
            courseFinisherCount = 282,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.now().plusDays(10),
        )
    }
}