package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.CourseListResponseDto
import com.choius323.saisai.ui.model.CourseInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface CourseRemoteDataSource {
    suspend fun getRecentCourse(): Result<CourseInfo>
    suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Result<CourseListResponseDto> // Ktor 응답 등을 직접 반환하거나, Result 래퍼를 사용할 수 있습니다. 여기서는 예시로 Result를 사용합니다.

}

class CourseRemoteDataSourceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val client: HttpClient,
) : CourseRemoteDataSource {
    override suspend fun getRecentCourse(): Result<CourseInfo> = withContext(ioDispatcher) {
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
        Result.success(mockCourse)
    }

    override suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Result<CourseListResponseDto> = withContext(ioDispatcher) {
        try {
            val response = client.get("http://43.202.239.148:8080/api/courses") {
                header(HttpHeaders.Authorization, "Bearer $tempAccessToken")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                parameter("page", page)
                level?.let { parameter("level", it) }
                distance?.let { parameter("distance", it) }
                sigun?.let { parameter("sigun", it) }
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private val tempAccessToken = "YOUR_ACCESS_TOKEN" // 임시 토큰
}