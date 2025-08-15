package com.choius323.saisai.ui.screen.settings


data class SettingsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGPSPermissionGranted: Boolean = false,
    val isShowLogOutDialog: Boolean = false,
    val isShowDeleteAccountDialog: Boolean = false
)

sealed interface SettingsUiEvent {
    data class OnGPSPermissionGranted(val isGranted: Boolean) : SettingsUiEvent
    data object OnClickLogOut : SettingsUiEvent
    data object OnClickDeleteAccount : SettingsUiEvent
    data class OnClickLogOutDialogButton(val isConfirmed: Boolean) : SettingsUiEvent
    data class OnClickDeleteAccountDialogButton(val isConfirmed: Boolean) : SettingsUiEvent
}

sealed interface SettingsSideEffect {
    data class ShowToast(val message: String) : SettingsSideEffect
    data object GoBack : SettingsSideEffect
}