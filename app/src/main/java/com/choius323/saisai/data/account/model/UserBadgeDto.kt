package com.choius323.saisai.data.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserBadgeDto(
    val id: Long,
    val name: String,
    @SerialName("image")
    val imageUrl: String? = null,
    val description: String,
    val condition: String,
)
