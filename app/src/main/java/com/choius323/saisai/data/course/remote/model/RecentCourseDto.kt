package com.choius323.saisai.data.course.remote.model


import kotlinx.serialization.Serializable

@Serializable
data class RecentCourseDto(
    val courseId: Long?,
    val courseImageUrl: String?,
    val courseName: String?,
    val distance: Double?,
    val progressRate: Double?,
    val recentRideAt: String?,
    val sigun: String?,
)