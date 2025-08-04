package com.choius323.saisai.data.account.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val nickname: String,
    val email: String,
    val rideCount: Int,
    val bookmarkCount: Int,
    val reward: Int,
    val badgeCount: Int,
    val imageUrl: String? = null,
)
