package com.choius323.saisai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choius323.saisai.data.account.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {
    val isLoggedIn: StateFlow<Boolean> = SessionManager.refreshToken
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionManager.refreshToken.value != null
        )
}
