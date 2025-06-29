package com.choius323.saisai.repository

import com.choius323.saisai.data.account.AccountRemoteDataSource
import com.choius323.saisai.ui.model.AccountToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AccountRepository {
    suspend fun login(email: String, password: String): Flow<Result<AccountToken>>
}

class AccountRepositoryImpl(
    private val accountDataSource: AccountRemoteDataSource,
) : AccountRepository {
    override suspend fun login(
        email: String, password: String,
    ): Flow<Result<AccountToken>> = accountDataSource.login(email, password).map { result ->
        result.mapCatching { responseDto ->
            println(responseDto.data)
            responseDto.data.toAccountToken()
        }
    }
}