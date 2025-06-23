package com.choius323.saisai.ui.model

data class CourseDetailInfo(
    val courseId: String,
    val courseName: String,
    val contents: String,
    val level: Int,
    val distanceInKm: Double, // km 단위로 표시 가정
    val estimatedTimeInHours: Double, // 시간 단위로 표시 가정
    val sigun: String,
    val tourInfo: String,
    val travelerInfo: String,
    val gpxPath: String,
    val completeUserCount: Int,
    val rewardInfo: RewardInfo?, // UI 모델 사용
)