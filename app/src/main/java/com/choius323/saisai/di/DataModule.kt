package com.choius323.saisai.di

import android.util.Log
import com.choius323.saisai.BuildConfig
import com.choius323.saisai.data.account.AccountLocalDataSource
import com.choius323.saisai.data.account.AccountLocalDataSourceImpl
import com.choius323.saisai.data.account.AccountRemoteDataSource
import com.choius323.saisai.data.account.AccountRemoteDataSourceImpl
import com.choius323.saisai.data.account.AuthDataStore
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.data.course.remote.CourseRemoteDataSourceImpl
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.repository.AccountRepositoryImpl
import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.repository.CourseRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val IO_DISPATCHER = "IoDispatcher"
private const val SAI_CLIENT = "SaiClient"
private const val DEFAULT_CLIENT = "DefaultClient"

object PrettyLogger : Logger {
    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            Log.d("KtorLogger", prettyJson(message)) // Pretty JSON
        } else {
            Log.d("KtorLogger", message) // Regular log
        }
    }
}

// Pretty Print JSON Function
private fun prettyJson(json: String): String {
    return try {
        when {
            json.startsWith("{") -> JSONObject(json).toString(4) // Format JSON Object
            json.startsWith("[") -> JSONArray(json).toString(4) // Format JSON Array
            else -> json
        }
    } catch (e: Exception) {
        json // Return as is if formatting fails
    }
}

object KtorClient {
    val defaultClient = HttpClient(OkHttp) {
        install(Logging) {
            logger = PrettyLogger
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true // JSON 로그 보기 좋게 출력 (개발 시 유용)
                isLenient = true // 약간의 JSON 문법 오류 허용
                ignoreUnknownKeys = true // 데이터 클래스에 정의되지 않은 키 무시
                coerceInputValues = true
            })
        }
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    fun saiClient() = this.defaultClient.config {
        defaultRequest {
            url(BuildConfig.SAI_BASE_URL)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = SessionManager.accessToken.value
                    val refreshToken = SessionManager.refreshToken.value
                    Log.d(
                        TAG,
                        "BearerTokens\naccessToken: $accessToken\nrefreshToken: $refreshToken"
                    )
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }
                refreshTokens {
                    Log.d(TAG, "start refreshTokens block")
                    val refreshToken = SessionManager.refreshToken.value ?: run {
                        SessionManager.onLogout()
                        return@refreshTokens null
                    }
                    val response = defaultClient.post("${BuildConfig.SAI_BASE_URL}auth/reissue") {
                        header(HttpHeaders.Authorization, "Bearer $refreshToken")
                    }
                    Log.d(TAG, "reissue refreshTokens: ${response.status}")
                    if (response.status.isSuccess()) {
                        val newTokens = response.body<SaiResponseDto<AccountTokenDto>>().data
                        SessionManager.onLoginSuccess(newTokens.accessToken, newTokens.refreshToken)

                        val accessToken = SessionManager.accessToken.value
                        val refreshToken = SessionManager.refreshToken.value
                        BearerTokens(accessToken!!, refreshToken)
                    } else {
                        SessionManager.onLogout()
                        null
                    }
                }
            }
        }
    }
}


val dataModule = module {
    single<CoroutineDispatcher>(named(IO_DISPATCHER)) { Dispatchers.IO }
    single<HttpClient>(named(DEFAULT_CLIENT)) { KtorClient.defaultClient }
    single<HttpClient>(named(SAI_CLIENT)) { KtorClient.saiClient() }

    single { AuthDataStore(androidContext()) }

    single<CourseRemoteDataSource> {
        CourseRemoteDataSourceImpl(
            get(named(SAI_CLIENT))
        )
    }
    single<CourseRepository> {
        CourseRepositoryImpl(
            get(named(IO_DISPATCHER)),
            get()
        )
    }
    single<AccountRemoteDataSource> {
        AccountRemoteDataSourceImpl(
            get(named(IO_DISPATCHER)),
            get(named(SAI_CLIENT)),
            get(named(DEFAULT_CLIENT))
        )
    }
    single<AccountLocalDataSource> { AccountLocalDataSourceImpl(get()) }
    single<AccountRepository> {
        AccountRepositoryImpl(get())
    }
}

private const val TAG = "DataModule"