package com.choius323.saisai.data.account

import android.content.Context
import com.choius323.saisai.BuildConfig
import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.account.model.LoginDto
import com.choius323.saisai.data.account.model.TotalRewardDto
import com.choius323.saisai.data.account.model.UserBadgeDetailDto
import com.choius323.saisai.data.account.model.UserBadgeDto
import com.choius323.saisai.data.account.model.UserInformationDto
import com.choius323.saisai.data.account.model.UserProfileDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.data.course.remote.saiFetch
import com.choius323.saisai.di.SaiClientProvider
import com.google.android.gms.auth.GoogleAuthUtil
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

interface AccountRemoteDataSource {
    suspend fun login(
        email: String,
        password: String,
    ): Flow<Result<SaiResponseDto<AccountTokenDto>>>

    suspend fun reissueToken(): Flow<Result<SaiResponseDto<AccountTokenDto>>>
    suspend fun getUserBadgeDetail(userBadgeId: Long): Flow<Result<SaiResponseDto<UserBadgeDetailDto>>>
    suspend fun getUserInfo(): Flow<Result<SaiResponseDto<UserInformationDto>>>
    suspend fun getUserBadgeList(): Flow<Result<SaiResponseDto<List<UserBadgeDto>>>>
    suspend fun getUserProfile(): Flow<Result<SaiResponseDto<UserProfileDto>>>
    suspend fun loginWithGoogle(idToken: String): Flow<Result<SaiResponseDto<AccountTokenDto>>>
    suspend fun loginWithKakao(accessToken: String): Flow<Result<SaiResponseDto<AccountTokenDto>>>
    suspend fun duplicateCheckNickname(nickname: String): Flow<Result<SaiResponseDto<Unit>>>
    suspend fun changeNickname(nickname: String): Flow<Result<SaiResponseDto<Unit>>>
    suspend fun getTotalReward(): Flow<Result<SaiResponseDto<TotalRewardDto>>>
    suspend fun logout(): Flow<Result<Unit>>
    suspend fun deleteAccount(socialAccessToken: String): Flow<Result<Unit>>
}

class AccountRemoteDataSourceImpl(
    private val saiClientProvider: SaiClientProvider,
    private val defaultClient: HttpClient,
    private val context: Context,
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
        saiFetch { saiClientProvider.client.get("badges/me/$userBadgeId") }

    override suspend fun getUserInfo():
            Flow<Result<SaiResponseDto<UserInformationDto>>> =
        saiFetch<SaiResponseDto<UserInformationDto>> { saiClientProvider.client.get("my") }

    override suspend fun getUserBadgeList(): Flow<Result<SaiResponseDto<List<UserBadgeDto>>>> =
        saiFetch { saiClientProvider.client.get("badges/me") }

    override suspend fun getUserProfile(): Flow<Result<SaiResponseDto<UserProfileDto>>> =
        saiFetch { saiClientProvider.client.get("my/profile") }

    override suspend fun loginWithGoogle(idToken: String): Flow<Result<SaiResponseDto<AccountTokenDto>>> =
        saiFetch {
            defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/login/google/android") {
                setBody(mapOf("token" to idToken))
            }
        }

    override suspend fun loginWithKakao(accessToken: String): Flow<Result<SaiResponseDto<AccountTokenDto>>> =
        saiFetch {
            defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/login/kakao") {
                setBody(mapOf("token" to accessToken))
            }
        }

    override suspend fun duplicateCheckNickname(nickname: String): Flow<Result<SaiResponseDto<Unit>>> =
        saiFetch {
            saiClientProvider.client.get("my/profile/nickname/check?nickname=$nickname")
        }

    override suspend fun changeNickname(nickname: String): Flow<Result<SaiResponseDto<Unit>>> =
        saiFetch {
            saiClientProvider.client.patch("my/profile/nickname") { setBody(mapOf("nickname" to nickname)) }
        }

    override suspend fun getTotalReward(): Flow<Result<SaiResponseDto<TotalRewardDto>>> =
        saiFetch { saiClientProvider.client.get("my/rewards") }

    override suspend fun logout(): Flow<Result<Unit>> =
        saiFetch {
            saiClientProvider.client.post("auth/logout")
        }

    override suspend fun deleteAccount(socialAccessToken: String): Flow<Result<Unit>> =
        saiFetch<Unit> {
            saiClientProvider.client.delete("auth/withdraw") {
                setBody(mapOf("socialAccessToken" to socialAccessToken))
            }
        }.onEach {
            GoogleAuthUtil.clearToken(context, socialAccessToken)
        }
}

private const val TAG = "AccountRemoteDataSource"