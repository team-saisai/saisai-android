package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseDetailDataDto(
    val courseId: String = "EMPTY",
    val courseName: String,
    val contents: String,
    val level: Int,
    val distance: Double,
    val estimatedTime: Double,
    val sigun: String,
    val tourInfo: String,
    val travelerInfo: String,
    val gpxpath: String,
    val completeUserCount: Int,
    val rewardInfo: RewardInfoDto? = null,
)
