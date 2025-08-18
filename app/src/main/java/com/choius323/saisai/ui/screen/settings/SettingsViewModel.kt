package com.choius323.saisai.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.LoginType
import com.choius323.saisai.usecase.LogoutUseCase
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SettingsViewModel(
    private val accountRepository: AccountRepository,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel(), ContainerHost<SettingsUiState, SettingsSideEffect> {
    override val container =
        container<SettingsUiState, SettingsSideEffect>(SettingsUiState())

    fun onEvent(event: SettingsUiEvent) = when (event) {
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
            logoutUseCase().collectLatest { result ->
                result.onFailure {
                    if (it.message != null) {
                        postSideEffect(SettingsSideEffect.ShowToast(it.message!!))
                    }
                }
                reduce { state.copy(isLoading = false) }
                postSideEffect(SettingsSideEffect.GoLogin)
            }
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

        is SettingsUiEvent.OnReLoginSuccess -> deleteAccount(
            event.loginType,
            event.socialAccessToken
        )
    }

    private fun onClickLogOutDialogButton(isConfirm: Boolean) = intent {
        reduce { state.copy(isLoading = true) }
        if (isConfirm) {
            logoutUseCase().collectLatest { result ->
                result.onFailure {
                    if (it.message != null) {
                        postSideEffect(SettingsSideEffect.ShowToast(it.message!!))
                    }
                }
                reduce { state.copy(isLoading = false) }
                postSideEffect(SettingsSideEffect.GoLogin)
            }
        } else {
            reduce { state.copy(isShowLogOutDialog = false, isLoading = false) }
        }
    }

    private fun onClickDeleteAccountDialogButton(isConfirm: Boolean) = intent {
        if (isConfirm) {
            accountRepository.getNowLoginType().collectLatest { loginType ->
                if (loginType != null) {
                    postSideEffect(SettingsSideEffect.ReLoginOAuth(LoginType.valueOf(loginType)))
                }
            }
        } else {
            reduce { state.copy(isShowDeleteAccountDialog = false) }
        }
    }

    private fun deleteAccount(loginType: LoginType, socialAccessToken: String) = intent {
        reduce { state.copy(isLoading = true) }
        accountRepository.deleteAccount(loginType.name, socialAccessToken).collectLatest { result ->
            result.onFailure {
                if (it.message != null) {
                    postSideEffect(SettingsSideEffect.ShowToast(it.message!!))
                }
            }
            reduce { state.copy(isLoading = false) }
            postSideEffect(SettingsSideEffect.GoLogin)
        }
    }
}