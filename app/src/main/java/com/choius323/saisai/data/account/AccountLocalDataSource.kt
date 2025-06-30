package com.choius323.saisai.data.account

import kotlinx.coroutines.flow.Flow

interface AccountLocalDataSource {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}

class AccountLocalDataSourceImpl(
    private val authDataStore: AuthDataStore,
) : AccountLocalDataSource {
    override val accessToken: Flow<String?>
        get() = authDataStore.accessToken

    override val refreshToken: Flow<String?>
        get() = authDataStore.refreshToken

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        authDataStore.saveTokens(accessToken, refreshToken)
    }

    override suspend fun clearTokens() {
        authDataStore.clearTokens()
    }
}
