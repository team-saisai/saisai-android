package com.choius323.saisai.data.account

import kotlinx.coroutines.flow.Flow

interface AccountLocalDataSource {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    val loginType: Flow<String?>
    suspend fun saveTokens(accessToken: String, refreshToken: String, loginType: String? = null)
    suspend fun clearTokens()
}

class AccountLocalDataSourceImpl(
    private val authDataStore: AuthDataStore,
) : AccountLocalDataSource {
    override val accessToken: Flow<String?>
        get() = authDataStore.accessToken

    override val refreshToken: Flow<String?>
        get() = authDataStore.refreshToken

    override val loginType: Flow<String?>
        get() = authDataStore.loginType

    override suspend fun saveTokens(accessToken: String, refreshToken: String, loginType: String?) {
        SessionManager.onLoginSuccess(accessToken, refreshToken, loginType)
    }

    override suspend fun clearTokens() {
        SessionManager.onLogout()
    }
}
