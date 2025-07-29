package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class Notification(
    val date: LocalDate,
    val imageUrl: String? = null,
    val content: String,
    val unread: Boolean,
) {
    companion object {
        val sample1 = Notification(LocalDate.of(2025, 7, 7), null, "새로운 챌린지가 시작되었어요!", false)
        val sample2 =
            Notification(LocalDate.of(2025, 7, 7), null, "코스 완주를 축하합니다!!!!! 뱃지를 확인해보세요.", false)
        val sample3 = Notification(LocalDate.of(2025, 7, 6), null, "주간 랭킹이 갱신되었습니다.", true)
        val sample4 = Notification(LocalDate.of(2025, 7, 5), null, "친구가 회원님을 팔로우하기 시작했습니다.", true)
        val sample5 = Notification(LocalDate.of(2025, 7, 4), null, "새로운 이벤트에 참여해보세요!", false)
        val sample6 = Notification(LocalDate.of(2025, 7, 3), null, "회원님의 기록이 정상적으로 저장되었습니다.", true)
    }
}