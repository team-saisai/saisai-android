package com.choius323.saisai.ui.screen.my_page

import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MyPageViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel(), ContainerHost<MyPageUiState, MyPageSideEffect> {

    override val container: Container<MyPageUiState, MyPageSideEffect> =
        container(MyPageUiState())

    fun onEvent(event: MyPageUiEvent) = when (event) {
        MyPageUiEvent.InitScreen -> loadData()
        MyPageUiEvent.OnClickProfile -> intent {
            postSideEffect(MyPageSideEffect.GoProfile)
        }

        MyPageUiEvent.OnClickAppSettings -> intent {
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

        MyPageUiEvent.OnClickTermsOfService -> intent {
            postSideEffect(MyPageSideEffect.GoTermsOfService)
        }

        MyPageUiEvent.OnClickNicknameEdit -> intent {
            postSideEffect(MyPageSideEffect.GoNicknameEdit(state.userProfile.nickname))
        }
    }

    private fun loadData() = intent {
        reduce { state.copy(isLoading = true) }
        accountRepository.getUserProfile().collectLatest { result ->
            result.onSuccess { profile ->
                reduce {
                    state.copy(userProfile = profile, isLoading = false)
                }
            }.onFailure { throwable ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(MyPageSideEffect.ShowToast(throwable.message ?: "프로필을 가져오지 못했습니다."))
            }
        }
    }
}