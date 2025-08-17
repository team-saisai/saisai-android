package com.choius323.saisai.data.course.remote

import com.choius323.saisai.data.course.remote.model.BookmarkDto
import com.choius323.saisai.data.course.remote.model.CompleteCourseDto
import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDto
import com.choius323.saisai.data.course.remote.model.CourseItemDto
import com.choius323.saisai.data.course.remote.model.DeleteBookmarkCoursesDto
import com.choius323.saisai.data.course.remote.model.RecentCourseDto
import com.choius323.saisai.data.course.remote.model.ResumeRideDto
import com.choius323.saisai.data.course.remote.model.RideHistoryDataDto
import com.choius323.saisai.data.course.remote.model.RideIdDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.data.course.remote.model.SaveRideDto
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
        page: Int, courseType: String, sort: String,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>>

    suspend fun resumeRide(rideId: Long): Flow<Result<SaiResponseDto<ResumeRideDto>>>
    suspend fun getCourseDetail(courseId: Long): Flow<Result<SaiResponseDto<CourseDetailDto>>>
    suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<CourseItemDto>>>>
    suspend fun startCourse(courseId: Long): Flow<Result<SaiResponseDto<RideIdDto>>>
    suspend fun deleteBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>>
    suspend fun addBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>>
    suspend fun getBookmarkedCourses(page: Int): Flow<Result<SaiResponseDto<CourseDataDto>>>
    suspend fun deleteBookmarkedCourses(body: DeleteBookmarkCoursesDto): Flow<Result<SaiResponseDto<Unit>>>
    suspend fun getRideHistory(page: Int, sort: String, notCompletedOnly: Boolean): Flow<Result<SaiResponseDto<RideHistoryDataDto>>>
    suspend fun deleteRideHistory(rideIds: List<Long>): Flow<Result<SaiResponseDto<Unit>>>
    suspend fun syncRide(rideId: Long, body: SaveRideDto): Flow<Result<SaiResponseDto<Unit?>>>
    suspend fun pauseRide(
        rideId: Long,
        body: SaveRideDto,
    ): Flow<Result<SaiResponseDto<RideIdDto>>>

    suspend fun completeCourse(
        rideId: Long, completeCourseDto: CompleteCourseDto,
    ): Flow<Result<SaiResponseDto<Unit?>>>
}

class CourseRemoteDataSourceImpl(
    private val client: HttpClient,
) : CourseRemoteDataSource {
    override suspend fun getRecentCourse(): Flow<Result<SaiResponseDto<RecentCourseDto?>>> =
        saiFetch { client.get("my/rides/recent") }

    override suspend fun getAllCourses(
        page: Int,
        courseType: String,
        sort: String,
    ): Flow<Result<SaiResponseDto<CourseDataDto>>> = saiFetch {
        client.get("courses") {
            parameter("page", page)
            parameter("type", courseType)
            parameter("sort", sort)
        }
    }

    override suspend fun getCourseDetail(
        courseId: Long,
    ): Flow<Result<SaiResponseDto<CourseDetailDto>>> = saiFetch { client.get("courses/$courseId") }

    override suspend fun getPopularChallenge(): Flow<Result<SaiResponseDto<List<CourseItemDto>>>> =
        saiFetch { client.get("challenges/popular") }

    override suspend fun startCourse(courseId: Long): Flow<Result<SaiResponseDto<RideIdDto>>> =
        saiFetch { client.post("courses/$courseId/rides") }

    override suspend fun completeCourse(
        rideId: Long, completeCourseDto: CompleteCourseDto,
    ): Flow<Result<SaiResponseDto<Unit?>>> = saiFetch {
        client.patch("rides/$rideId/complete") {
            setBody(completeCourseDto)
        }
    }

    override suspend fun resumeRide(rideId: Long): Flow<Result<SaiResponseDto<ResumeRideDto>>> =
        saiFetch { client.patch("rides/$rideId/resume") }

    override suspend fun pauseRide(
        rideId: Long,
        body: SaveRideDto,
    ): Flow<Result<SaiResponseDto<RideIdDto>>> = saiFetch {
        client.patch("rides/$rideId/pause") {
            setBody(body)
        }
    }

    override suspend fun addBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>> =
        saiFetch { client.post("courses/$courseId/bookmarks") }

    override suspend fun deleteBookmark(courseId: Long): Flow<Result<SaiResponseDto<BookmarkDto>>> =
        saiFetch { client.delete("courses/$courseId/bookmarks") }

    override suspend fun getBookmarkedCourses(page: Int): Flow<Result<SaiResponseDto<CourseDataDto>>> =
        saiFetch { client.get("my/bookmarks/courses?page=$page") }

    override suspend fun deleteBookmarkedCourses(body: DeleteBookmarkCoursesDto): Flow<Result<SaiResponseDto<Unit>>> =
        saiFetch { client.delete("my/bookmarks/courses") { setBody(body) } }

    override suspend fun getRideHistory(page: Int, sort: String, notCompletedOnly: Boolean): Flow<Result<SaiResponseDto<RideHistoryDataDto>>> =
        saiFetch { client.get("my/rides?page=$page&sort=$sort&notCompletedOnly=$notCompletedOnly") }

    override suspend fun deleteRideHistory(rideIds: List<Long>): Flow<Result<SaiResponseDto<Unit>>> =
        saiFetch { client.delete("my/rides") { setBody(mapOf("rideIds" to rideIds)) } }

    override suspend fun syncRide(
        rideId: Long, body: SaveRideDto
    ): Flow<Result<SaiResponseDto<Unit?>>> =
        saiFetch { client.patch("rides/$rideId/sync") { setBody(body) } }
}