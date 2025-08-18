package com.choius323.saisai.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.ui.model.LoginType
import com.choius323.saisai.usecase.LoginUseCase
import com.choius323.saisai.usecase.ReissueTokenUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val reissueTokenUseCase: ReissueTokenUseCase,
) : ViewModel(), ContainerHost<LoginUiState, LoginSideEffect> {
    override val container: Container<LoginUiState, LoginSideEffect> = container(LoginUiState())

    init {
        intent {
            viewModelScope.launch {
                delay(2000L)
                reduce { state.copy(isDelayed = true) }
            }
            reduce { state.copy(isLoading = true) }
            if (SessionManager.refreshToken.value != null) {
                reissueTokenUseCase().collectLatest { result ->
                    result.onSuccess {
                        reduce { state.copy(isLoginSuccess = true) }
                    }.onFailure {
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(LoginSideEffect.ShowToast(it.message ?: "Unknown Error"))
                    }
                }
            } else {
                SessionManager.onLogout()
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: LoginUiEvent) = when (event) {
        is LoginUiEvent.SuccessOAuthLogin -> intent {
            login(event.loginType, event.token)
        }
    }

    private fun login(loginType: LoginType, token: String) = intent {
        reduce { state.copy(isLoading = true) }
        loginUseCase(token, loginType).collectLatest { result ->
            result.onSuccess {
                reduce { state.copy(isLoginSuccess = true) }
            }.onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginSideEffect.ShowToast(it.message ?: "Unknown Error"))
            }
        }
    }
}