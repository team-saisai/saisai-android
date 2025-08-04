package com.choius323.saisai.ui.model

data class UserProfile(
    val imageUrl: String?,
    val nickname: String,
    val email: String,
    val rideCount: Int,
    val bookmarkCount: Int,
    val reward: Int,
    val badgeCount: Int,
)