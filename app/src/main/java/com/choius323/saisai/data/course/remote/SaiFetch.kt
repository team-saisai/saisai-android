package com.choius323.saisai.data.course.remote

import android.util.Log
import com.choius323.saisai.data.course.remote.model.SaiErrorResponseDto
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

inline fun <reified T> saiFetch(crossinline block: suspend () -> HttpResponse): Flow<Result<T>> =
    flow {
        emit(runCatching {
            val response = block()
            if (response.status.isSuccess()) {
                response.body<T>()
            } else {
                val errorToThrow = runCatching {
                    val errorBody = response.body<SaiErrorResponseDto>()
                    IllegalStateException("${errorBody.code} - ${errorBody.message}")
                }.getOrElse { exception ->
                    IOException("Network error: ${response.status.value} - ${response.status.description}. ${exception.message}")
                }
                throw errorToThrow
            }
        }.recoverCatching { throwable ->
            Log.e("saiFetch", "Error fetching data", throwable)
            when (throwable) {
                is SocketTimeoutException -> {
                    throw SocketTimeoutException("네트워크 연결이 불안정합니다. 잠시 후 다시 시도해주세요.")
                }

                else -> {
                    throw IOException(throwable)
                }
            }
        })
    }