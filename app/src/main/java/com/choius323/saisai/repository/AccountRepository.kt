package com.choius323.saisai.repository

import com.choius323.saisai.data.account.AccountLocalDataSource
import com.choius323.saisai.data.account.AccountRemoteDataSource
import com.choius323.saisai.ui.model.AccountToken
import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.model.UserBadgeDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AccountRepository {
    suspend fun login(email: String, password: String): Flow<Result<AccountToken>>
    suspend fun reissueToken(): Flow<Result<AccountToken>>
    suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<UserBadgeDetail>>
    suspend fun getUserInfo(): Flow<Result<String>>
    suspend fun getUserBadgeList(): Flow<Result<List<UserBadge>>>
}

class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
) : AccountRepository {
    override suspend fun login(
        email: String, password: String,
    ): Flow<Result<AccountToken>> = accountRemoteDataSource.login(email, password).map { result ->
        result.mapCatching { responseDto ->
            responseDto.data.toAccountToken().apply {
                accountLocalDataSource.saveTokens(accessToken, refreshToken)
            }
        }
    }

    override suspend fun reissueToken(): Flow<Result<AccountToken>> =
        accountRemoteDataSource.reissueToken().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toAccountToken().apply {
                    accountLocalDataSource.saveTokens(accessToken, refreshToken)
                }
            }
        }

    override suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<UserBadgeDetail>> =
        accountRemoteDataSource.getUserBadgeDetail(userBadgeId).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toUserBadgeDetail()
            }
        }

    override suspend fun getUserInfo(): Flow<Result<String>> =
        accountRemoteDataSource.getUserInfo().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.nickname
            }
        }

    override suspend fun getUserBadgeList(): Flow<Result<List<UserBadge>>> =
        accountRemoteDataSource.getUserBadgeList().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toUserBadgeList()
            }
        }
}