package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckPointDto(
    val latitude: Double,
    val longitude: Double,
    val gpxPathIdx: Int,
)
