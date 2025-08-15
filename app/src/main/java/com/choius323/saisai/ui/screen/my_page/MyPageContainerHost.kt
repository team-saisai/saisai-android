package com.choius323.saisai.ui.screen.my_page

import com.choius323.saisai.ui.model.UserProfile

data class MyPageUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userProfile: UserProfile = UserProfile(null, "", "", 0, 0, 0, 0),
)

sealed interface MyPageUiEvent {
    data object OnClickBookmarkCourses : MyPageUiEvent
    data object OnClickRideCourses : MyPageUiEvent
    data object OnClickRewardHistory : MyPageUiEvent
    data object OnClickBadgeAchievement : MyPageUiEvent
    data object OnClickAppSettings : MyPageUiEvent
    data object OnClickProfile : MyPageUiEvent
    data object OnClickTermsOfService : MyPageUiEvent
    data object OnClickNicknameEdit : MyPageUiEvent
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
    data object GoTermsOfService : MyPageSideEffect
    data class GoNicknameEdit(val nickname: String) : MyPageSideEffect
}