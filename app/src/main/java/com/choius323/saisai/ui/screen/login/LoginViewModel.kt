package com.choius323.saisai.ui.screen.login

import androidx.lifecycle.ViewModel
import com.choius323.saisai.usecase.FetchLoginUseCase
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class LoginViewModel(
    private val fetchLoginUseCase: FetchLoginUseCase,
) : ViewModel(), ContainerHost<LoginUiState, LoginSideEffect> {
    override val container: Container<LoginUiState, LoginSideEffect> = container(LoginUiState())

    fun onEvent(event: LoginUiEvent) = when (event) {
        is LoginUiEvent.LoginButtonClicked -> login()
        is LoginUiEvent.EmailChanged -> intent {
            reduce { state.copy(email = event.email) }
        }

        is LoginUiEvent.PasswordChanged -> intent {
            reduce { state.copy(password = event.password) }
        }
    }

    private fun login() = intent {
        val email = state.email
        val password = state.password
        reduce { state.copy(isLoading = true) }
        fetchLoginUseCase(email, password).collectLatest { result ->
            result.onSuccess {
                postSideEffect(LoginSideEffect.LoginSuccess)
            }.onFailure {
                reduce { state.copy(isLoading = false, error = it.message ?: "Unknown Error") }
            }
        }
    }
}