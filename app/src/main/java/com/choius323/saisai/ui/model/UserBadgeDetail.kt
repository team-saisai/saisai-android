package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class UserBadgeDetail(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val acquiredDate: LocalDate,
)