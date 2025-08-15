package com.choius323.saisai.ui.screen.nickname_edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Error
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NicknameEditUiState(
    val nickname: String = "",
    val currentNickname: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNicknameValid: Boolean = false,
    val isDuplicateCheckDone: Boolean = false,
    val maxNicknameLength: Int = 10,
    val supportingTextType: SupportingTextType = SupportingTextType.NONE
)

sealed interface NicknameEditUiEvent {
    data class OnNicknameChanged(val nickname: String) : NicknameEditUiEvent
    data object OnClickDuplicateCheck : NicknameEditUiEvent
    data object OnClickDone : NicknameEditUiEvent
    data object OnClickBack : NicknameEditUiEvent
}

sealed interface NicknameEditSideEffect {
    data object NavigateBack : NicknameEditSideEffect
    data class ShowToast(val message: String) : NicknameEditSideEffect
    data object NicknameUpdateSuccess : NicknameEditSideEffect
}

enum class SupportingTextType(
    val text: String?,
    val color: Color?,
    val icon: ImageVector?
) {
    NONE(
        text = null,
        color = null,
        icon = null
    ),
    CHECK_SUCCESS(
        text = "사용 가능한 닉네임입니다.",
        color = Color(0xFF8976ED),
        icon = Icons.Outlined.CheckCircle
    ),
    DUPLICATED(
        text = "사용 중인 닉네임입니다.",
        color = Color(0xFFFF7676),
        icon = Icons.Outlined.Error
    ),
//    NICKNAME_LENGTH_EXCEEDED,
    ;
}