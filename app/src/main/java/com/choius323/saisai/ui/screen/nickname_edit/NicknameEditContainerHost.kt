package com.choius323.saisai.ui.screen.nickname_edit

data class NicknameEditUiState(
    val nickname: String = "",
    val currentNickname: String = "",
    val isLoading: Boolean = false,
    val isPatternValid: Boolean = false,
    val isLengthValid: Boolean = false,
) {
    val isValid: Boolean
        get() = isPatternValid && isLengthValid
}

sealed interface NicknameEditUiEvent {
    data class OnNicknameChanged(val nickname: String) : NicknameEditUiEvent
    data object OnClickDone : NicknameEditUiEvent
    data object OnClickBack : NicknameEditUiEvent
}

sealed interface NicknameEditSideEffect {
    data object NavigateBack : NicknameEditSideEffect
    data class ShowToast(val message: String) : NicknameEditSideEffect
    data object NicknameUpdateSuccess : NicknameEditSideEffect
}