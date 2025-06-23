package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SaiErrorResponseDto(
    val code: String,
    val message: String,
)