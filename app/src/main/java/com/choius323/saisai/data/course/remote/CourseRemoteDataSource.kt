package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDto
import com.choius323.saisai.data.course.remote.model.PopularChallengeItemDto
import com.choius323.saisai.data.course.remote.model.RecentCourseDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.parameters
import kotlinx.coroutines.flow.Flow

interface CourseRemoteDataSource {
    suspend fun getRecentCourse(): Flow<Result<SaiResponseDto<RecentCourseDto?>>>
    suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>>

    suspend fun getCourseDetail(courseId: Long): Flow<Result<SaiResponseDto<CourseDetailDto>>>
    suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<PopularChallengeItemDto>>>>
}

class CourseRemoteDataSourceImpl(
    private val client: HttpClient,
) : CourseRemoteDataSource {
    override suspend fun getRecentCourse(): Flow<Result<SaiResponseDto<RecentCourseDto?>>> =
        saiFetch(client.get("my/rides"))

    override suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>> = saiFetch(client.get("courses") {
        parameters {
            if (status != null) append("status", status)
        }
    })

    override suspend fun getCourseDetail(
        courseId: Long,
    ): Flow<Result<SaiResponseDto<CourseDetailDto>>> =
        saiFetch(client.get("courses/$courseId"))

    override suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<PopularChallengeItemDto>>>> =
        saiFetch(client.get("challenges/popular"))
}