package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDataDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.CourseListItem
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.parameters
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CourseRemoteDataSource {
    suspend fun getRecentCourse(): Flow<Result<CourseListItem>>
    suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>>

    suspend fun getCourseDetail(courseName: String): Flow<Result<SaiResponseDto<CourseDetailDataDto>>>
}

class CourseRemoteDataSourceImpl(
    private val client: HttpClient,
) : CourseRemoteDataSource {
    override suspend fun getRecentCourse(): Flow<Result<CourseListItem>> = flow {
        delay(1000)
        val mockCourse = CourseListItem(
            courseId = "COURSE_ID_67890",
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "한강 수영장 - 여의도",
            summary = "dapibus",
            level = 1,
            distance = 5.2,
            estimatedTime = 30.0,
            sigun = "서울시 강남구",
            challengeInfo = null,
        )
        emit(Result.success(mockCourse))
    }

    override suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>> = saiFetch(client.get("courses") {
        parameters {
            if (status != null) append("status", status)
        }
    })

    override suspend fun getCourseDetail(
        courseName: String,
    ): Flow<Result<SaiResponseDto<CourseDetailDataDto>>> =
        saiFetch(client.get("courses/$courseName"))

    private val tempAccessToken = "YOUR_ACCESS_TOKEN" // 임시 토큰
}