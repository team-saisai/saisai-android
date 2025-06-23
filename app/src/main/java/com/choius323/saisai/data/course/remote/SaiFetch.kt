package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.SaiErrorResponseDto
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <reified T> saiFetch(response: HttpResponse): Flow<Result<T>> = flow {
    emit(runCatching {
        if (response.status.isSuccess()) {
            response.body<T>()
        } else {
            val errorToThrow = runCatching {
                val errorBody = response.body<SaiErrorResponseDto>()
                Exception("${errorBody.code} - ${errorBody.message}")
            }.getOrElse { exception ->
                Exception("Network error: ${response.status.value} - ${response.status.description}. ${exception.message}")
            }
            throw errorToThrow
        }
    })
}