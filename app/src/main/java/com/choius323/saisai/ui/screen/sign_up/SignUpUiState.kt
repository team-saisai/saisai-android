package com.choius323.saisai.ui.screen.sign_up

import com.choius323.saisai.ui.model.LoginType

data class SignUpUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCheckedTermOfService: Boolean = false,
    val isCheckedAge: Boolean = false,
    val loginType: LoginType = LoginType.KAKAO,
    val token: String = "",
) {
    val isAllChecked: Boolean
        get() = isCheckedAge && isCheckedTermOfService
}

sealed interface SignUpUiEvent {
    data object OnClickSignUpButton : SignUpUiEvent
    data object OnClickTermOfServiceCheckBox : SignUpUiEvent
    data object OnClickAgeCheckBox : SignUpUiEvent
    data object OnClickTermOfServiceDetail : SignUpUiEvent
    data object OnClickAllCheckBox : SignUpUiEvent
    data object OnClickBack : SignUpUiEvent
}

sealed interface SignUpSideEffect {
    data class ShowToast(val message: String) : SignUpSideEffect
    data object GoTermOfServiceDetail : SignUpSideEffect
    data object GoBack : SignUpSideEffect
    data object GoHome : SignUpSideEffect
}