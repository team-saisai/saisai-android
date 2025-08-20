package com.choius323.saisai.data.account.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountTokenDto(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean = false,
)