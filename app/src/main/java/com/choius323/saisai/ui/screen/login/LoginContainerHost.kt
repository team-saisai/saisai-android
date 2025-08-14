package com.choius323.saisai.ui.screen.login

import com.choius323.saisai.ui.model.LoginType

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface LoginUiEvent {
    data class SuccessOAuthLogin(val loginType: LoginType, val token: String) : LoginUiEvent
}

sealed interface LoginSideEffect {
    data object LoginSuccess : LoginSideEffect
    data class ShowToast(val message: String) : LoginSideEffect
}