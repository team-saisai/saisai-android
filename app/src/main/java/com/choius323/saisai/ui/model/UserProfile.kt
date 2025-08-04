package com.choius323.saisai.ui.model

data class UserProfile(
    val imageUrl: String?,
    val nickname: String,
    val email: String,
    val rideCount: Int,
    val bookmarkCount: Int,
    val reward: Int,
    val badgeCount: Int,
) {
    companion object {
        val sample1 = UserProfile(
            imageUrl = null,
            nickname = "사용자",
            email = "asd@email.com",
            rideCount = 24,
            bookmarkCount = 62,
            reward = 2821,
            badgeCount = 5
        )
    }
}