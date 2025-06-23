package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeInfoDto(
    val challengeStatus: String,
    val challengeEndedTime: String,
)
