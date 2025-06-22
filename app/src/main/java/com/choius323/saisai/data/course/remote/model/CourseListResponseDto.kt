package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseListResponseDto(
    val code: String,
    val message: String,
    val data: CourseDataDto,
)
