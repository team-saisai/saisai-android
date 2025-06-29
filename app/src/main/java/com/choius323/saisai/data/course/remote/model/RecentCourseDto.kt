package com.choius323.saisai.data.course.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentCourseDto(
    @SerialName("courseIamgeUrl")
    val courseImageUrl: String,
    val courseName: String,
    val distance: Double,
    val progressRate: Double,
    val recentRideAt: String,
    val sigun: String,
)