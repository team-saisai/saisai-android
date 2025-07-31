package com.choius323.saisai.data.account

import com.choius323.saisai.BuildConfig
import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.account.model.LoginDto
import com.choius323.saisai.data.account.model.UserBadgeDetailDto
import com.choius323.saisai.data.account.model.UserBadgeDto
import com.choius323.saisai.data.account.model.UserInformationDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.data.course.remote.saiFetch
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow

interface AccountRemoteDataSource {
    suspend fun login(
        email: String,
        password: String,
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>>

    suspend fun reissueToken(): Flow<Result<SaiResponseDto<AccountTokenDto>>>
    suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<SaiResponseDto<UserBadgeDetailDto>>>
    suspend fun getUserInfo(): Flow<Result<SaiResponseDto<UserInformationDto>>>
    suspend fun getUserBadgeList(): Flow<Result<SaiResponseDto<List<UserBadgeDto>>>>
}

class AccountRemoteDataSourceImpl(
    private val client: HttpClient,
    private val defaultClient: HttpClient,
) : AccountRemoteDataSource {
    override suspend fun login(
        email: String,
        password: String,
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>> =
        saiFetch<SaiResponseDto<AccountTokenDto>> {
            defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/login") {
            setBody(LoginDto(email, password))
            }
        }

    override suspend fun reissueToken(
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>> = saiFetch<SaiResponseDto<AccountTokenDto>> {
        defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/reissue") {
            header(HttpHeaders.Authorization, "Bearer ${SessionManager.refreshToken.value}")
        }
    }

    override suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<SaiResponseDto<UserBadgeDetailDto>>> =
        saiFetch { client.get("badges/me/$userBadgeId") }

    override suspend fun getUserInfo():
            Flow<Result<SaiResponseDto<UserInformationDto>>> =
        saiFetch<SaiResponseDto<UserInformationDto>> { client.get("my") }

    override suspend fun getUserBadgeList(): Flow<Result<SaiResponseDto<List<UserBadgeDto>>>> =
        saiFetch { client.get("badges/me") }
}

private const val TAG = "AccountRemoteDataSource"