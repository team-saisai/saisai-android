package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RewardInfoDto(
    val rewardName: String,
    val rewardImageUrl: String,
    val startedAt: String,
    val endedAt: String,
)