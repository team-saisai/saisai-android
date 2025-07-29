package com.choius323.saisai.ui.screen.my_page

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MyPageViewModel(
    // TODO: UseCases, Repository 등 주입
) : ViewModel(), ContainerHost<MyPageUiState, MyPageSideEffect> {

    override val container: Container<MyPageUiState, MyPageSideEffect> =
        container(MyPageUiState())

    init {
        // TODO: 실제 API 데이터 호출
        intent {
            reduce {
                state.copy(
                    name = "델라",
                    email = "saisai@naver.com",
                    rewardPoint = 1280,
                    badgeCount = 6,
                    rideCount = 132,
                    bookmarkCount = 24,
                )
            }
        }
    }

    // TODO: 이벤트 로직 추가
    fun onEvent(event: MyPageUiEvent) = when (event) {
        MyPageUiEvent.OnClickProfile -> {}
        MyPageUiEvent.OnClickSetting -> {}
        MyPageUiEvent.OnClickRideCourses -> {}
        MyPageUiEvent.OnClickBookmarkCourses -> {}
        MyPageUiEvent.OnClickRewardHistory -> {}
        MyPageUiEvent.OnClickBadgeAchievement -> intent {
            postSideEffect(MyPageSideEffect.GoBadgeAchievement)
        }
    }
}