package com.choius323.saisai.data.account

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object SessionManager {
    @SuppressLint("StaticFieldLeak")
    private lateinit var authDataStore: AuthDataStore

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken = _accessToken.asStateFlow()

    private val _refreshToken = MutableStateFlow<String?>(null)
    val refreshToken = _refreshToken.asStateFlow()

    private val _loginType = MutableStateFlow<String?>(null)
    val loginType = _loginType.asStateFlow()

    fun init(authDataStore: AuthDataStore) {
        this.authDataStore = authDataStore

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                authDataStore.accessToken.collectLatest {
                    _accessToken.emit(it)
                }
            }
            launch {
                authDataStore.refreshToken.collectLatest {
                    _refreshToken.emit(it)
                }
            }
            launch {
                authDataStore.loginType.collectLatest {
                    _loginType.emit(it)
                }
            }
        }
    }

    // 로그인 성공 시 호출
    suspend fun onLoginSuccess(accessToken: String, refreshToken: String, loginType: String?) {
        authDataStore.saveTokens(accessToken, refreshToken, loginType)
    }

    // 로그아웃 시 호출
    suspend fun onLogout() {
        authDataStore.clearTokens()
    }
}

private const val TAG = "SessionManager"