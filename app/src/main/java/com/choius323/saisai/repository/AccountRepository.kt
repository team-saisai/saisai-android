package com.choius323.saisai.repository

import com.choius323.saisai.data.account.AccountLocalDataSource
import com.choius323.saisai.data.account.AccountRemoteDataSource
import com.choius323.saisai.ui.model.AccountToken
import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.model.UserBadgeDetail
import com.choius323.saisai.ui.model.UserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface AccountRepository {
    suspend fun login(email: String, password: String): Flow<Result<AccountToken>>
    suspend fun reissueToken(): Flow<Result<AccountToken>>
    suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<UserBadgeDetail>>
    suspend fun getUserInfo(): Flow<Result<String>>
    suspend fun getUserBadgeList(): Flow<Result<List<UserBadge>>>
    suspend fun getUserProfile(): Flow<Result<UserProfile>>
    suspend fun loginWithGoogle(idToken: String): Flow<Result<AccountToken>>
    suspend fun loginWithKakao(kakaoAccessToken: String): Flow<Result<AccountToken>>
    suspend fun saveToken(accountToken: AccountToken)
    suspend fun logOut()
}

class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : AccountRepository {
    override suspend fun login(
        email: String, password: String,
    ): Flow<Result<AccountToken>> = accountRemoteDataSource.login(email, password).map { result ->
        result.mapCatching { responseDto ->
            responseDto.data.toAccountToken().apply {
                accountLocalDataSource.saveTokens(accessToken, refreshToken)
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun reissueToken(): Flow<Result<AccountToken>> =
        accountRemoteDataSource.reissueToken().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toAccountToken().apply {
                    accountLocalDataSource.saveTokens(accessToken, refreshToken)
                }
            }
        }.flowOn(ioDispatcher)

    override suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<UserBadgeDetail>> =
        accountRemoteDataSource.getUserBadgeDetail(userBadgeId).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toUserBadgeDetail()
            }
        }.flowOn(ioDispatcher)

    override suspend fun getUserInfo(): Flow<Result<String>> =
        accountRemoteDataSource.getUserInfo().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.nickname
            }
        }.flowOn(ioDispatcher)

    override suspend fun getUserBadgeList(): Flow<Result<List<UserBadge>>> =
        accountRemoteDataSource.getUserBadgeList().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toUserBadgeList()
            }
        }.flowOn(ioDispatcher)

    override suspend fun getUserProfile(): Flow<Result<UserProfile>> =
        accountRemoteDataSource.getUserProfile().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toUserProfile()
            }
        }.flowOn(ioDispatcher)

    override suspend fun loginWithGoogle(idToken: String): Flow<Result<AccountToken>> =
        accountRemoteDataSource.loginWithGoogle(idToken).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toAccountToken()
            }
        }.flowOn(ioDispatcher)

    override suspend fun loginWithKakao(kakaoAccessToken: String): Flow<Result<AccountToken>> =
        accountRemoteDataSource.loginWithKakao(kakaoAccessToken).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toAccountToken()
            }
        }.flowOn(ioDispatcher)

    override suspend fun saveToken(accountToken: AccountToken) {
        accountLocalDataSource.saveTokens(accountToken.accessToken, accountToken.refreshToken)
    }

    override suspend fun logOut() {
        accountLocalDataSource.clearTokens()
    }
}