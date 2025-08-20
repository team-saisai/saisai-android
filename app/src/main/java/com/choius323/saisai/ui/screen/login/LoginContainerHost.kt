package com.choius323.saisai.ui.screen.login

import com.choius323.saisai.ui.model.LoginType

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNewUser: Boolean = false,
    val isDelayed: Boolean = false,
)

sealed interface LoginUiEvent {
    data class SuccessOAuthLogin(val loginType: LoginType, val token: String) : LoginUiEvent
}

sealed interface LoginSideEffect {
    data class ShowToast(val message: String) : LoginSideEffect
    data class GoSignUp(val token: String, val loginType: LoginType) : LoginSideEffect
    data object GoHome : LoginSideEffect
}