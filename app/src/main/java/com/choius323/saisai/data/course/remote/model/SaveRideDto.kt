package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveRideDto(
    val duration: Long,
    val checkpointIdx: Int,
)