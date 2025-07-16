package com.choius323.saisai.data.account.model

import kotlinx.serialization.Serializable

@Serializable
data class UserBadgeDto(
    val badgeName: String,
    val badgeDescription: String,
    val badgeImage: String,
    val acquiredAt: String,
)