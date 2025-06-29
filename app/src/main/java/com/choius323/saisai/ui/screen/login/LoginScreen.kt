package com.choius323.saisai.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    goHome: () -> Unit,
) {
    val uiState = viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.LoginSuccess -> goHome()
        }
    }

    LoginScreenContent(
        uiState = uiState.value,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onEvent: (LoginUiEvent) -> Unit,
) {
    Column(modifier) {
        TextField(
            uiState.email,
            onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            uiState.password,
            onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
        )
        Button({ onEvent(LoginUiEvent.LoginButtonClicked) }) {
            Text("로그인")
        }
    }
}