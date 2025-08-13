package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    val latitude: Double,
    val longitude: Double
)
