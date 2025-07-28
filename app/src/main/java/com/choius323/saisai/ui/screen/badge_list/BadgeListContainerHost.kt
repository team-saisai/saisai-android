package com.choius323.saisai.ui.screen.badge_list

import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.model.UserBadgeDetail

data class BadgeListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val badgeList: List<UserBadge> = emptyList(),
    val showBadgeDetail: UserBadgeDetail? = null,
)

sealed interface BadgeListUiEvent {
    data class OnClickBadge(val badgeId: Long) : BadgeListUiEvent
    data object OnClickBack : BadgeListUiEvent
    data object CloseBadgeDialog : BadgeListUiEvent
}

sealed interface BadgeListSideEffect {
    data class ShowToast(val message: String) : BadgeListSideEffect
    data object GoBack : BadgeListSideEffect
}