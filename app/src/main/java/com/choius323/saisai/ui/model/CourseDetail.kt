package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class CourseDetail(
    val courseId: Long,
    val courseName: String,
    val summary: String,
    val level: Level,
    val distance: Double, // km 단위로 표시 가정
    val estimatedTime: Double, // 분 단위로 표시 가정
    val sigun: String,
    val imageUrl: String?,
    val inProgressUserCount: Int,
    val completeUserCount: Int,
    val rideId: Long?,
    val challengeStatus: String?,
    val challengeEndedAt: LocalDate?,
    val isEventActive: Boolean,
    val gpxPointList: List<GpxPoint>,
    val checkPointList: List<CheckPoint> = emptyList(),
    val duration: Long? = null,
    val checkpointIdx: Int? = null,
) {
    companion object {
        val sample = CourseDetail(
            courseId = 215,
            courseName = "서해랑길 44코스",
            summary = "- 람사르 습지로 등록된 갯벌이 살아 숨쉬는 줄포만생태갯벌공원을 지나는 코스 \n- 다양한 조류와 염생 식물, 갯벌동물을 만날 수 있는 코스",
            level = Level.Medium,
            distance = 14.0,
            estimatedTime = 151.0,
            sigun = "전북 부안군",
            imageUrl = null,
            inProgressUserCount = 2,
            completeUserCount = 2,
            gpxPointList = GpxPoint.gpxPointsSample,
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.of(2026, 5, 13),
            isEventActive = true,
            rideId = 1,
            checkPointList = CheckPoint.sample1
        )
    }
}