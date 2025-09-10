package com.choius323.saisai.ui.model

import com.choius323.saisai.R

data class UserBadge(
    val id: Long,
    val name: String,
    val imageUrl: Any?,
    val description: String,
    val condition: String,
) {
    companion object {
        val sample1 = UserBadge(
            id = 1,
            name = "천리 길도\n한 바퀴부터",
            imageUrl = R.drawable.img_badge_lock,
            description = "사이사이에서의 완벽한 첫 걸음! 앞으로 함께 나아갈 길이 기대됩니다.",
            condition = "첫 코스 완주 시 "
        )
        val sample2 = UserBadge(
            id = 2,
            name = "천하제일라이더",
            imageUrl = null,
            description = "사이사이에서의 완벽한 첫 걸음! 앞으로 함께 나아갈 길이 기대됩니다.",
            condition = "첫 코스 완주 시 "
        )
        val sample3 = UserBadge(
            id = 3,
            name = "강호도장깨기",
            imageUrl = null,
            description = "사이사이에서의 완벽한 첫 걸음! 앞으로 함께 나아갈 길이 기대됩니다.",
            condition = "첫 코스 완주 시 "
        )
        val sample4 = UserBadge(
            id = 4,
            name = "천리 길도 한 바퀴부터",
            imageUrl = null,
            description = "사이사이에서의 완벽한 첫 걸음! 앞으로 함께 나아갈 길이 기대됩니다.",
            condition = "첫 코스 완주 시 "
        )
    }
}
