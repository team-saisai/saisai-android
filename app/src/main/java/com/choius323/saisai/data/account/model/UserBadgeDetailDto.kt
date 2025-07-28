package com.choius323.saisai.data.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserBadgeDetailDto(
    val badgeName: String,
    val badgeDescription: String,
    @SerialName("badgeImageUrl")
    val imageUrl: String? = null,
    val acquiredAt: String,
)