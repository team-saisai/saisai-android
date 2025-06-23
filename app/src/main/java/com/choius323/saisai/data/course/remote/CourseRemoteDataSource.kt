package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDataDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.CourseInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CourseRemoteDataSource {
    suspend fun getRecentCourse(): Flow<Result<CourseInfo>>
    suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>>

    suspend fun getCourseDetail(courseName: String): Flow<Result<SaiResponseDto<CourseDetailDataDto>>>
}

class CourseRemoteDataSourceImpl(
    private val client: HttpClient,
) : CourseRemoteDataSource {
    override suspend fun getRecentCourse(): Flow<Result<CourseInfo>> = flow {
        delay(1000)
        val mockCourse = CourseInfo(
            courseId = 1,
            imageUrl = "https://picsum.photos/seed/cycling1/400/300",
            endDate = "12/31",
            startPlace = "서울",
            endPlace = "부산",
            distance = "450km",
            difficulty = "상",
            challengerCount = 120,
            themes = listOf("국토종주", "자전거길"),
            completedCount = 75
        )
        emit(Result.success(mockCourse))
    }

    override suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>> = flow {
        emit(runCatching {
            val response = client.get("courses") {
                header(HttpHeaders.Authorization, "Bearer $tempAccessToken")
                parameter("page", page)
                level?.let { parameter("level", it) }
                distance?.let { parameter("distance", it) }
                sigun?.let { parameter("sigun", it) }
            }
            if (response.status.value in 200..299) {
                Result.success(response.body<SaiResponseDto<CourseDataDto>>())
            } else {
                Result.failure(Exception("Network error: ${response.status.value} - ${response.status.description}"))
            }
        }.getOrElse { e ->
            Result.failure(e)
        })
    }

    override suspend fun getCourseDetail(
        courseName: String,
    ): Flow<Result<SaiResponseDto<CourseDetailDataDto>>> = flow {
        emit(runCatching {
            val response = client.get("courses") {
                header(HttpHeaders.Authorization, "Bearer $tempAccessToken")
                parameter("courseName", courseName)
            }
            if (response.status.value in 200..299) {
                Result.success(response.body<SaiResponseDto<CourseDetailDataDto>>())
            } else {
                Result.failure(Exception("Network error: ${response.status.value} - ${response.status.description}"))
            }
        }.getOrElse { e ->
            Result.failure(e)
        })
    }

    private val tempAccessToken = "YOUR_ACCESS_TOKEN" // 임시 토큰
}