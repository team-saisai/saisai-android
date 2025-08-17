package com.choius323.saisai.data.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TotalRewardDto(
    val totalReward: Int,

    @SerialName("rewardInfos")
    val rewardInfoList: List<RewardInfoDto>,
)

@Serializable
data class RewardInfoDto(
    val reward: Int,
    val acquiredAt: String,
    val courseName: String
)