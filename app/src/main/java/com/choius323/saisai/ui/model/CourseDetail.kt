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
    val rideId: Int?,
    val challengeStatus: String,
    val challengeEndedAt: LocalDate,
    val isEventActive: Boolean,
    val gpxPointList: List<GpxPoint>,
    val checkPointList: List<GpxPoint> = emptyList(),
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
            gpxPointList = listOf(
                GpxPoint(
                    lat = 35.54751103743911,
                    lng = 126.67916360311212,
                    elevation = 5.92,
                    segmentDistance = 0.0,
                    totalDistance = 0.0
                ),
                GpxPoint(
                    lat = 35.54757398553194,
                    lng = 126.67913493700327,
                    elevation = 5.467,
                    segmentDistance = 7.464532505563946,
                    totalDistance = 0.007464532505563946
                ),
                GpxPoint(
                    lat = 35.54773215204478,
                    lng = 126.67886554263534,
                    elevation = 4.329,
                    segmentDistance = 30.055559406760533,
                    totalDistance = 0.03752009191232448
                ),
                GpxPoint(
                    lat = 35.54793164134024,
                    lng = 126.67857368476687,
                    elevation = 4.0,
                    segmentDistance = 34.48572992715891,
                    totalDistance = 0.07200582183948338
                ),
                GpxPoint(
                    lat = 35.5481077451259,
                    lng = 126.67840705253185,
                    elevation = 4.138,
                    segmentDistance = 24.712705717248543,
                    totalDistance = 0.09671852755673192
                ),
                GpxPoint(
                    lat = 35.54829281754791,
                    lng = 126.67826355434956,
                    elevation = 4.854,
                    segmentDistance = 24.3319520161327,
                    totalDistance = 0.12105047957286462
                )
            ),
            challengeStatus = "ONGOING",
            challengeEndedAt = LocalDate.of(2026, 5, 13),
            isEventActive = true,
            rideId = 1
        )
    }
}