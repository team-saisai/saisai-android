package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.BookmarkDto
import com.choius323.saisai.data.course.remote.model.CompleteCourseDto
import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDto
import com.choius323.saisai.data.course.remote.model.PauseRideDto
import com.choius323.saisai.data.course.remote.model.PopularChallengeItemDto
import com.choius323.saisai.data.course.remote.model.RecentCourseDto
import com.choius323.saisai.data.course.remote.model.RideIdDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow

interface CourseRemoteDataSource {
    suspend fun getRecentCourse(): Flow<Result<SaiResponseDto<RecentCourseDto?>>>
    suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>>

    suspend fun resumeRide(rideId: Long): Flow<Result<SaiResponseDto<RideIdDto>>>
    suspend fun getCourseDetail(courseId: Long): Flow<Result<SaiResponseDto<CourseDetailDto>>>
    suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<PopularChallengeItemDto>>>>
    suspend fun startCourse(courseId: Long): Flow<Result<SaiResponseDto<RideIdDto>>>
    suspend fun deleteBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>>
    suspend fun addBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>>
    suspend fun pauseRide(
        rideId: Long,
        pauseRideDto: PauseRideDto,
    ): Flow<Result<SaiResponseDto<RideIdDto>>>

    suspend fun completeCourse(
        rideId: Long, completeCourseDto: CompleteCourseDto,
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

    override suspend fun startCourse(courseId: Long): Flow<Result<SaiResponseDto<RideIdDto>>> =
        saiFetch(client.post("courses/$courseId/rides"))

    override suspend fun completeCourse(
        rideId: Long, completeCourseDto: CompleteCourseDto,
    ): Flow<Result<SaiResponseDto<Unit>>> =
        saiFetch(
            client.patch("rides/$rideId/complete") {
                setBody(completeCourseDto)
            }
        )

    override suspend fun resumeRide(rideId: Long): Flow<Result<SaiResponseDto<RideIdDto>>> =
        saiFetch(client.patch("rides/$rideId/resume"))

    override suspend fun pauseRide(
        rideId: Long,
        pauseRideDto: PauseRideDto,
    ): Flow<Result<SaiResponseDto<RideIdDto>>> =
        saiFetch(client.patch("rides/$rideId/pause") {
            setBody(pauseRideDto)
        })

    override suspend fun addBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>> =
        saiFetch(client.post("courses/$courseId/bookmarks"))

    override suspend fun deleteBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>> =
        saiFetch(client.delete("courses/$courseId/bookmarks"))
}