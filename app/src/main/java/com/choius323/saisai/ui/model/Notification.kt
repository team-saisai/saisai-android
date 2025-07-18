package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class Notification(
    val date: LocalDate,
    val imageUrl: String? = null,
    val content: String,
    val unread: Boolean,
)