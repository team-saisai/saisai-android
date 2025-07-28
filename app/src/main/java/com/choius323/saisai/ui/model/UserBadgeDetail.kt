package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class UserBadgeDetail(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val acquiredDate: LocalDate,
) {
    companion object {
        val sample1 = UserBadgeDetail(
            name = "천리 길도 한 바퀴부터",
            description = "사이사이에서의 완벽한 첫 걸음!\n앞으로 함께 나아갈 길이 기대됩니다.",
            imageUrl = null,
            acquiredDate = LocalDate.of(2025, 5, 23)
        )
    }
}