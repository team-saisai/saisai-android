package com.choius323.saisai.repository

import com.choius323.saisai.data.account.AccountRemoteDataSource
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.ui.model.AccountToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AccountRepository {
    suspend fun login(email: String, password: String): Flow<Result<AccountToken>>

    suspend fun reissueToken(): Flow<Result<AccountToken>>
}

class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource,
) : AccountRepository {
    override suspend fun login(
        email: String, password: String,
    ): Flow<Result<AccountToken>> = accountRemoteDataSource.login(email, password).map { result ->
        result.mapCatching { responseDto ->
            responseDto.data.toAccountToken().apply {
                SessionManager.onLoginSuccess(accessToken, refreshToken)
            }
        }
    }

    override suspend fun reissueToken(): Flow<Result<AccountToken>> =
        accountRemoteDataSource.reissueToken().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toAccountToken().apply {
                    SessionManager.onLoginSuccess(accessToken, refreshToken)
                }
            }
        }
}