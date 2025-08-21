package com.choius323.saisai.ui.screen.nickname_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.navigation.MainNavItem
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class NicknameEditViewModel(
    private val accountRepository: AccountRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<NicknameEditUiState, NicknameEditSideEffect> {
    override val container =
        container<NicknameEditUiState, NicknameEditSideEffect>(NicknameEditUiState())

    init {
        val nickname = savedStateHandle.toRoute<MainNavItem.NicknameEdit>().nickname
        intent {
            reduce { state.copy(currentNickname = nickname) }
            onNicknameChanged(nickname)
        }
    }

    fun onEvent(event: NicknameEditUiEvent) {
        when (event) {
            is NicknameEditUiEvent.OnNicknameChanged -> onNicknameChanged(event.nickname)
            NicknameEditUiEvent.OnClickDone -> onClickDone()
            NicknameEditUiEvent.OnClickBack -> intent {
                postSideEffect(NicknameEditSideEffect.NavigateBack)
            }
        }
    }

    private fun onClickDone() = intent {
        if (state.isValid.not()) return@intent
        reduce { state.copy(isLoading = true) }

        accountRepository.changeNickname(state.nickname).collectLatest { result ->
            result.onSuccess {
                reduce { state.copy(isLoading = false) }
                postSideEffect(NicknameEditSideEffect.NicknameUpdateSuccess)
            }.onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(NicknameEditSideEffect.ShowToast(it.message ?: "닉네임 변경에 실패했습니다."))
            }
        }
    }

    private fun onNicknameChanged(inputNickname: String) = intent {
        val newNickname =
            inputNickname.filterNot { it.isWhitespace() }.take(NICKNAME_MAX_LENGTH)
        reduce {
            state.copy(
                nickname = newNickname,
                isLengthValid = newNickname.length in 1..NICKNAME_MAX_LENGTH,
                isPatternValid = NICKNAME_PATTERN.matches(newNickname),
            )
        }
    }

    companion object {
        private val NICKNAME_PATTERN = "^[가-힣a-zA-Z0-9]+$".toRegex()
        private const val NICKNAME_MAX_LENGTH = 7
    }
}

