package com.choius323.saisai.data.account

import com.choius323.saisai.BuildConfig
import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.account.model.LoginDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.data.course.remote.saiFetch
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface AccountRemoteDataSource {
    suspend fun login(
        email: String,
        password: String,
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>>

    suspend fun reissueToken(): Flow<Result<SaiResponseDto<AccountTokenDto>>>
}

class AccountRemoteDataSourceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val client: HttpClient,
    private val defaultClient: HttpClient,
) : AccountRemoteDataSource {
    override suspend fun login(
        email: String,
        password: String,
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>> =
        saiFetch<SaiResponseDto<AccountTokenDto>>(defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/login") {
            setBody(LoginDto(email, password))
        }).flowOn(ioDispatcher)

    override suspend fun reissueToken(
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>> = saiFetch<SaiResponseDto<AccountTokenDto>>(
        defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/reissue") {
            header(HttpHeaders.Authorization, "Bearer ${SessionManager.refreshToken.value}")
        }).flowOn(ioDispatcher)
}

private const val TAG = "AccountRemoteDataSource"