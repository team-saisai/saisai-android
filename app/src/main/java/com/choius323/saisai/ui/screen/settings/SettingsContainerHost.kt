package com.choius323.saisai.ui.screen.settings

import com.choius323.saisai.ui.model.LoginType


data class SettingsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGPSPermissionGranted: Boolean = false,
    val isShowLogOutDialog: Boolean = false,
    val isShowDeleteAccountDialog: Boolean = false,
    val isShowPermissionDialog: Boolean = false,
)

sealed interface SettingsUiEvent {
    data class OnGPSPermissionGranted(val isGranted: Boolean) : SettingsUiEvent
    data object OnClickLogOut : SettingsUiEvent
    data object OnClickDeleteAccount : SettingsUiEvent
    data class OnClickLogOutDialogButton(val isConfirmed: Boolean) : SettingsUiEvent
    data class OnClickDeleteAccountDialogButton(val isConfirmed: Boolean) : SettingsUiEvent
    data object OnLogOutSuccess : SettingsUiEvent
    data object OnDeleteAccountSuccess : SettingsUiEvent
    data class OnAccountManageFailed(val message: String) : SettingsUiEvent
    data class OnReLoginSuccess(val loginType: LoginType, val socialAccessToken: String) :
        SettingsUiEvent

    data class SetPermissionGranted(val isGranted: Boolean) : SettingsUiEvent
    data class SetShowPermissionDialog(val isShow: Boolean) : SettingsUiEvent
    data object PermissionRequest : SettingsUiEvent
}

sealed interface SettingsSideEffect {
    data class ShowToast(val message: String) : SettingsSideEffect
    data object GoBack : SettingsSideEffect
    data object GoLogin : SettingsSideEffect
    data class ReLoginOAuth(val loginType: LoginType) :
        SettingsSideEffect
    data object PermissionRequest : SettingsSideEffect
}