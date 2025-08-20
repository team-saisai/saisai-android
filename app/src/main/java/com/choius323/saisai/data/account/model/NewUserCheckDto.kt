package com.choius323.saisai.data.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUserCheckDto(
    val isNewUser: Boolean
)
