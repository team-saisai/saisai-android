package com.choius323.saisai.data.account.model

import kotlinx.serialization.Serializable

@Serializable
data class UserBadgeDto(
    val userBadgeId: Long,
    val badgeName: String,
    val badgeImageUrl: String,
)
