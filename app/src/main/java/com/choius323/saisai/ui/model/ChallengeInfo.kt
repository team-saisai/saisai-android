package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDateTime

data class ChallengeInfo(
    val challengeStatus: String,
    val challengeEndedTime: LocalDateTime,
)