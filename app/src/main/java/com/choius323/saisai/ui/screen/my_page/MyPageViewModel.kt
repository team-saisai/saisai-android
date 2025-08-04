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
        MyPageUiEvent.OnClickProfile -> intent {
            postSideEffect(MyPageSideEffect.GoProfile)
        }

        MyPageUiEvent.OnClickSetting -> intent {
            postSideEffect(MyPageSideEffect.GoSetting)
        }

        MyPageUiEvent.OnClickRideCourses -> intent {
            postSideEffect(MyPageSideEffect.GoRodeListCourses)
        }

        MyPageUiEvent.OnClickBookmarkCourses -> intent {
            postSideEffect(MyPageSideEffect.GoBookmarkCourses)
        }

        MyPageUiEvent.OnClickRewardHistory -> intent {
            postSideEffect(MyPageSideEffect.GoRewardHistory)
        }
        MyPageUiEvent.OnClickBadgeAchievement -> intent {
            postSideEffect(MyPageSideEffect.GoBadgeAchievement)
        }
    }
}