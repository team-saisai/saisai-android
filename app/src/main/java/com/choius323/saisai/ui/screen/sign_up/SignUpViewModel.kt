package com.choius323.saisai.ui.screen.sign_up

import androidx.lifecycle.ViewModel
import com.choius323.saisai.data.account.SessionManager
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SignUpViewModel(
) : ViewModel(), ContainerHost<SignUpUiState, SignUpSideEffect> {
    override val container: Container<SignUpUiState, SignUpSideEffect> =
        container(SignUpUiState())

    fun onEvent(event: SignUpUiEvent) = when (event) {
        SignUpUiEvent.OnClickSignUpButton -> intent {
            postSideEffect(SignUpSideEffect.GoHome)
        }

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
            SessionManager.onLogout()
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
}