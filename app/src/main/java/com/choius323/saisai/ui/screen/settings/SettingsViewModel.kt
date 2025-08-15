package com.choius323.saisai.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.LoginType
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SettingsViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel(), ContainerHost<SettingsUiState, SettingsSideEffect> {
    override val container =
        container<SettingsUiState, SettingsSideEffect>(SettingsUiState())

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnClickLogOutDialogButton -> onClickLogOutDialogButton(event.isConfirmed)
            is SettingsUiEvent.OnClickDeleteAccountDialogButton ->
                onClickDeleteAccountDialogButton(event.isConfirmed)

            is SettingsUiEvent.OnGPSPermissionGranted -> intent {
                reduce { state.copy(isGPSPermissionGranted = event.isGranted) }
            }

            SettingsUiEvent.OnClickLogOut -> intent {
                reduce { state.copy(isShowLogOutDialog = true) }
            }

            SettingsUiEvent.OnClickDeleteAccount -> intent {
                reduce { state.copy(isShowDeleteAccountDialog = true) }
            }

            SettingsUiEvent.OnLogOutSuccess, SettingsUiEvent.OnDeleteAccountSuccess -> intent {
                accountRepository.logOut()
            }

            is SettingsUiEvent.OnAccountManageFailed -> intent {
                reduce {
                    state.copy(
                        isShowLogOutDialog = false,
                        isShowDeleteAccountDialog = false,
                        isLoading = false
                    )
                }
                postSideEffect(SettingsSideEffect.ShowToast(event.message))
            }
        }
    }

    private fun onClickLogOutDialogButton(isConfirm: Boolean) = intent {
        reduce { state.copy(isLoading = true) }
        if (isConfirm) {
            accountRepository.getNowLoginType().collectLatest { loginType ->
                if (loginType != null) {
                    postSideEffect(SettingsSideEffect.LogOutOAuth(LoginType.valueOf(loginType)))
                } else {
                    accountRepository.logOut()
                }
            }
        } else {
            reduce { state.copy(isShowLogOutDialog = false, isLoading = false) }
        }
    }

    private fun onClickDeleteAccountDialogButton(isConfirm: Boolean) = intent {
        reduce { state.copy(isLoading = true) }
        if (isConfirm) {
            accountRepository.getNowLoginType().collectLatest { loginType ->
                if (loginType != null) {
                    postSideEffect(SettingsSideEffect.DeleteAccountOAuth(LoginType.valueOf(loginType)))
                } else {
                    accountRepository.logOut()
                }
            }
        } else {
            reduce { state.copy(isShowDeleteAccountDialog = false, isLoading = false) }
        }
    }
}