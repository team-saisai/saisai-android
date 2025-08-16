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
            reduce { state.copy(currentNickname = nickname, nickname = nickname) }
        }
    }

    fun onEvent(event: NicknameEditUiEvent) {
        when (event) {
            is NicknameEditUiEvent.OnNicknameChanged -> onNicknameChanged(event.nickname)
            NicknameEditUiEvent.OnClickDuplicateCheck -> onClickDuplicateCheck()
            NicknameEditUiEvent.OnClickDone -> onClickDone()
            NicknameEditUiEvent.OnClickBack -> intent {
                postSideEffect(NicknameEditSideEffect.NavigateBack)
            }
        }
    }

    private fun onClickDone() = intent {
        if (state.isDuplicateCheckDone.not()) return@intent
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

    private fun onClickDuplicateCheck() {
        intent {
            if (state.isDuplicateCheckDone || state.isNicknameValid.not()) return@intent
            reduce { state.copy(isLoading = true) }

            accountRepository.duplicateCheckNickname(state.nickname).collectLatest { result ->
                result.onSuccess {
                    reduce {
                        state.copy(
                            isDuplicateCheckDone = true,
                            supportingTextType = SupportingTextType.CHECK_SUCCESS,
                            isLoading = false,
                        )
                    }
                }.onFailure { throwable ->
                    if (throwable.message?.contains("USER_ER_03") == true) {
                        reduce {
                            state.copy(
                                isDuplicateCheckDone = false,
                                isNicknameValid = false,
                                isLoading = false,
                                supportingTextType = SupportingTextType.DUPLICATED,
                            )
                        }
                    } else {
                        reduce {
                            state.copy(
                                isDuplicateCheckDone = false,
                                isNicknameValid = false,
                                isLoading = false,
                                supportingTextType = SupportingTextType.NONE,
                            )
                        }
                        postSideEffect(
                            NicknameEditSideEffect.ShowToast(
                                throwable.message ?: "닉네임을 변경하지 못했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    private fun onNicknameChanged(inputNickname: String) = intent {
        val newNickname =
            inputNickname.take(state.maxNicknameLength).filterNot { it.isWhitespace() }
        val isValid =
            newNickname.length in 1..state.maxNicknameLength && newNickname != state.currentNickname
        reduce {
            state.copy(
                nickname = newNickname,
                isNicknameValid = isValid,
                isDuplicateCheckDone = false,
                supportingTextType = SupportingTextType.NONE,
            )
        }
    }
}
