package com.choius323.saisai.ui.screen.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val email: String = "",
    val password: String = "",
)

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    object LoginButtonClicked : LoginUiEvent
}

sealed interface LoginSideEffect {
    object LoginSuccess : LoginSideEffect
}