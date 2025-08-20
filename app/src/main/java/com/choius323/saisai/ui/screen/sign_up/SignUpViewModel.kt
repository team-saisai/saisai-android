package com.choius323.saisai.ui.screen.sign_up

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.choius323.saisai.ui.model.LoginType
import com.choius323.saisai.ui.navigation.MainNavItem
import com.choius323.saisai.usecase.AccessServiceUseCase
import com.choius323.saisai.usecase.LoginUseCase
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SignUpViewModel(
    private val accessServiceUseCase: AccessServiceUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<SignUpUiState, SignUpSideEffect> {
    override val container: Container<SignUpUiState, SignUpSideEffect> =
        container(SignUpUiState())

    init {
        val signUp = savedStateHandle.toRoute<MainNavItem.SignUp>()
        intent {
            reduce {
                state.copy(
                    loginType = LoginType.valueOf(signUp.loginType), token = signUp.token
                )
            }
        }
    }

    fun onEvent(event: SignUpUiEvent) = when (event) {
        SignUpUiEvent.OnClickSignUpButton -> signUp()

        SignUpUiEvent.OnClickTermOfServiceCheckBox -> intent {
            reduce { state.copy(isCheckedTermOfService = state.isCheckedTermOfService.not()) }
        }

        SignUpUiEvent.OnClickAgeCheckBox -> intent {
            reduce { state.copy(isCheckedAge = state.isCheckedAge.not()) }
        }

        SignUpUiEvent.OnClickTermOfServiceDetail -> intent {
            postSideEffect(SignUpSideEffect.GoTermOfServiceDetail)
        }

        SignUpUiEvent.OnClickBack -> intent {
            postSideEffect(SignUpSideEffect.GoBack)
        }

        SignUpUiEvent.OnClickAllCheckBox -> onChangeCheckBox()
    }

    private fun onChangeCheckBox() = intent {
        if (state.isAllChecked) {
            reduce { state.copy(isCheckedAge = false, isCheckedTermOfService = false) }
        } else {
            reduce { state.copy(isCheckedAge = false, isCheckedTermOfService = false) }
        }
    }

    private fun signUp() = intent {
        reduce { state.copy(isLoading = true) }
        accessServiceUseCase(state.token, state.loginType).collectLatest { result ->
            result.onSuccess {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SignUpSideEffect.GoHome)
            }.onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SignUpSideEffect.ShowToast(it.message ?: "회원가입에 실패했습니다."))
            }
        }
    }
}