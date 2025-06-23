package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDataDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.CourseInfo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
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
    ): Flow<Result<SaiResponseDto<CourseDataDto>>> = saiFetch(client.get("courses"))

    override suspend fun getCourseDetail(
        courseName: String,
    ): Flow<Result<SaiResponseDto<CourseDetailDataDto>>> =
        saiFetch(client.get("courses/$courseName"))

    private val tempAccessToken = "YOUR_ACCESS_TOKEN" // 임시 토큰
}