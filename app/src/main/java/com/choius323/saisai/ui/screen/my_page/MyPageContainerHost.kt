package com.choius323.saisai.ui.screen.my_page

data class MyPageUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val email: String = "",
    val profileImage: String? = null,
    val rewardPoint: Int = 0,
    val badgeCount: Int = 0,
    val rideCount: Int = 0,
    val bookmarkCount: Int = 0,
)

sealed interface MyPageUiEvent {
    data object OnClickBookmarkCourses : MyPageUiEvent
    data object OnClickRideCourses : MyPageUiEvent
    data object OnClickRewardHistory : MyPageUiEvent
    data object OnClickBadgeAchievement : MyPageUiEvent
    data object OnClickSetting : MyPageUiEvent
    data object OnClickProfile : MyPageUiEvent
}

sealed interface MyPageSideEffect {
    data class ShowToast(val message: String) : MyPageSideEffect
    data object GoBookmarkCourses : MyPageSideEffect
    data object GoRodeListCourses : MyPageSideEffect
    data object GoRewardHistory : MyPageSideEffect
    data object GoBadgeAchievement : MyPageSideEffect
    data object GoSetting : MyPageSideEffect
    data object GoProfile : MyPageSideEffect
    data object GoNotification : MyPageSideEffect
}