package com.choius323.saisai.data.account.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val email: String,
    val password: String,
)
