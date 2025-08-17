package com.choius323.saisai.ui.screen.total_reward

import com.choius323.saisai.ui.model.RewardInfo


data class TotalRewardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalPoints: Int = 0,
    val rewardInfoList: List<RewardInfo> = emptyList()
)

sealed interface TotalRewardUiEvent {
    data object OnClickBack : TotalRewardUiEvent
}

sealed interface TotalRewardSideEffect {
    data object GoBack : TotalRewardSideEffect
    data class ShowToast(val message: String) : TotalRewardSideEffect
}