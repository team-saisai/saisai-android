package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GpxPointDto(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    val segmentDistance: Double,
)