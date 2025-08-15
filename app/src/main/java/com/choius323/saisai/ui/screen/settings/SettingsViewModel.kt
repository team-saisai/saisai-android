package com.choius323.saisai.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SettingsViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel(), ContainerHost<SettingsUiState, SettingsSideEffect> {
    override val container =
        container<SettingsUiState, SettingsSideEffect>(SettingsUiState())

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnClickLogOutDialogButton -> {}
            is SettingsUiEvent.OnClickDeleteAccountDialogButton -> {}
            is SettingsUiEvent.OnGPSPermissionGranted -> intent {
                reduce { state.copy(isGPSPermissionGranted = event.isGranted) }
            }

            SettingsUiEvent.OnClickLogOut -> intent {
                reduce { state.copy(isShowLogOutDialog = true) }
            }

            SettingsUiEvent.OnClickDeleteAccount -> intent {
                reduce { state.copy(isShowDeleteAccountDialog = true) }
            }
        }
    }

    private fun onClickLogOutDialogButton(isConfirm: Boolean) = intent {
        if (isConfirm) {
            accountRepository.logOut()
        } else {
            reduce { state.copy(isShowLogOutDialog = false) }
        }
    }

    private fun onClickDeleteAccountDialogButton(isConfirm: Boolean) = intent {
        if (isConfirm) {
            // TODO: accountRepository.deleteAccount()
        } else {
            reduce { state.copy(isShowDeleteAccountDialog = false) }
        }
    }
}