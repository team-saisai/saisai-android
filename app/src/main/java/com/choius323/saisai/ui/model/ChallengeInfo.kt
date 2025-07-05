package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class ChallengeInfo(
    val challengeStatus: String,
    val challengeEndedTime: LocalDate,
)