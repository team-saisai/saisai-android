package com.choius323.saisai.data.course.remote

import android.webkit.MimeTypeMap
import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDto
import com.choius323.saisai.data.course.remote.model.PopularChallengeItemDto
import com.choius323.saisai.data.course.remote.model.RecentCourseDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CourseRemoteDataSource {
    suspend fun getRecentCourse(): Flow<Result<SaiResponseDto<RecentCourseDto?>>>
    suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>>

    suspend fun getCourseDetail(courseId: Long): Flow<Result<SaiResponseDto<CourseDetailDto>>>
    suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<PopularChallengeItemDto>>>>
    suspend fun startCourse(courseId: Long): Flow<Result<SaiResponseDto<Long>>>
    suspend fun completeCourse(
        rideId: Long, duration: Long, distance: Double, image: File,
    ): Flow<Result<SaiResponseDto<Unit>>>
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
        parameter("page", "$page")
        if (status != null) parameter("status", status)
    })

    override suspend fun getCourseDetail(
        courseId: Long,
    ): Flow<Result<SaiResponseDto<CourseDetailDto>>> = saiFetch(client.get("courses/$courseId"))

    override suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<PopularChallengeItemDto>>>> =
        saiFetch(client.get("challenges/popular"))

    override suspend fun startCourse(courseId: Long): Flow<Result<SaiResponseDto<Long>>> =
        saiFetch(client.patch("courses/$courseId/rides"))

    override suspend fun completeCourse(
        rideId: Long, duration: Long, distance: Double, image: File,
    ): Flow<Result<SaiResponseDto<Unit>>> {
        val mimeType =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(image.extension) ?: "image/*"
        return saiFetch(
            client.patch("rides/$rideId/complete") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("duration", duration)
                            append("actualDistance", distance)
                            append(
                                "completedImage", image.readBytes(), Headers.build {
                                    append(HttpHeaders.ContentType, mimeType)
                                    append(HttpHeaders.ContentDisposition, "filename=${image.name}")
                                })
                        })
                )
            })
    }
}