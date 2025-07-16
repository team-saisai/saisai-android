package com.choius323.saisai.data.course.remote.model

import org.threeten.bp.LocalDate

data class UserBadge(
    val name: String,
    val description: String,
    val imageUrl: String,
    val acquiredDate: LocalDate,
)