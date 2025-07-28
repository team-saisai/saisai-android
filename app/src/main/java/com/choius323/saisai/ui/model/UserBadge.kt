package com.choius323.saisai.ui.model

data class UserBadge(
    val id: Long,
    val name: String,
    val imageUrl: String?,
) {
    companion object {
        val sample1 = UserBadge(
            id = 1,
            name = "천리 길도\n한 바퀴부터",
            imageUrl = null,
        )
        val sample2 = UserBadge(
            id = 2,
            name = "천하제일라이더",
            imageUrl = null,
        )
        val sample3 = UserBadge(
            id = 3,
            name = "강호도장깨기",
            imageUrl = null,
        )
        val sample4 = UserBadge(
            id = 4,
            name = "자주 보는 사이",
            imageUrl = null,
        )
        val sample5 = UserBadge(
            id = 5,
            name = "상상이상",
            imageUrl = null,
        )
        val sample6 = UserBadge(
            id = 6,
            name = "다시 본 사이",
            imageUrl = null,
        )
        val sample7 = UserBadge(
            id = 7,
            name = "행운이 깃든 자",
            imageUrl = null,
        )
    }
}
