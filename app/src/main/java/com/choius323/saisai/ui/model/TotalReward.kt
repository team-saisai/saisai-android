package com.choius323.saisai.ui.model

import org.threeten.bp.LocalDate

data class TotalReward(
    val totalReward: Int,
    val rewardInfoList: List<RewardInfo>,
)

data class RewardInfo(
    val reward: Int,
    val acquiredAt: LocalDate,
    val courseName: String
) {
    companion object {
        val sample1 = RewardInfo(
            reward = 500,
            acquiredAt = LocalDate.of(2025, 7, 10),
            courseName = "특별 이벤트 보상"
        )
        val sample2 =
            RewardInfo(
                reward = 300,
                acquiredAt = LocalDate.of(2025, 7, 9),
                courseName = "장거리 라이딩 완료"
            )
        val sample3 =
            RewardInfo(
                reward = 450,
                acquiredAt = LocalDate.of(2025, 7, 8),
                courseName = "주간 목표 달성"
            )
    }
}