package com.choius323.saisai.repository

import com.choius323.saisai.data.course.local.CourseLocalDataSource
import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.data.course.remote.model.CompleteCourseDto
import com.choius323.saisai.data.course.remote.model.PauseRideDto
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.CoursePage
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.model.RecentRide
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface CourseRepository {
    suspend fun getRecentCourse(): Flow<Result<RecentCourse?>>
    suspend fun getRecentRideCourse(): Flow<Result<RecentRide>>
    suspend fun getCourseDetail(courseId: Long): Flow<Result<CourseDetail>>
    suspend fun getPopularChallenge(): Flow<Result<List<PopularChallengeListItem>>>
    suspend fun startCourse(courseId: Long): Flow<Result<Long>>
    suspend fun setRecentRide(recentRide: RecentRide)
    suspend fun getRecentRide(): Flow<Result<RecentRide>>
    suspend fun resumeRide(rideId: Long): Flow<Result<Unit>>
    suspend fun pauseRide(rideId: Long, duration: Long, totalDistance: Double): Flow<Result<Unit>>
    suspend fun deleteBookmark(courseId: Long): Flow<Result<Boolean>>
    suspend fun addBookmark(courseId: Long): Flow<Result<Boolean>>
    suspend fun getAllCourses(
        page: Int,
        status: String? = null,
    ): Flow<Result<CoursePage>>

    suspend fun completeCourse(
        rideId: Long, duration: Long, distance: Double,
    ): Flow<Result<Unit>>
}

class CourseRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val courseRemoteDataSource: CourseRemoteDataSource,
    private val courseLocalDataSource: CourseLocalDataSource,
) : CourseRepository {
    override suspend fun getRecentCourse(): Flow<Result<RecentCourse?>> =
        courseRemoteDataSource.getRecentCourse().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data?.toRecentCourse()
            }
        }.flowOn(ioDispatcher)

    override suspend fun getRecentRideCourse(): Flow<Result<RecentRide>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCourses(
        page: Int,
        status: String?,
    ): Flow<Result<CoursePage>> =
        courseRemoteDataSource.getAllCourses(page, status).map { result ->
            result.mapCatching { responseDto ->
                responseDto.toCoursePage()
            }
        }.flowOn(ioDispatcher)

    override suspend fun getCourseDetail(courseId: Long): Flow<Result<CourseDetail>> =
        courseRemoteDataSource.getCourseDetail(courseId).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toCourseDetail()
            }
        }.flowOn(ioDispatcher)

    override suspend fun getPopularChallenge(): Flow<Result<List<PopularChallengeListItem>>> =
        courseRemoteDataSource.getPopularChallenge().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.map { it.toPopularChallengeListItem() }
            }
        }.flowOn(ioDispatcher)

    override suspend fun startCourse(courseId: Long): Flow<Result<Long>> =
        courseRemoteDataSource.startCourse(courseId).map { result ->
            result.mapCatching { responseDto -> responseDto.data.rideId }
        }.flowOn(ioDispatcher)

    override suspend fun completeCourse(
        rideId: Long, duration: Long, distance: Double,
    ): Flow<Result<Unit>> =
        courseRemoteDataSource.completeCourse(rideId, CompleteCourseDto(duration, distance))
            .map { result ->
                result.mapCatching { responseDto -> responseDto.data }
            }.flowOn(ioDispatcher)

    override suspend fun setRecentRide(recentRide: RecentRide) {
        courseLocalDataSource.setRecentRideCourse(recentRide.toProto())
    }

    override suspend fun getRecentRide(): Flow<Result<RecentRide>> {
        return courseLocalDataSource.getRecentRideCourse().map { result ->
            Result.success(result.toDomainModel())
        }.catch {
            emit(Result.failure<RecentRide>(it))
        }.flowOn(ioDispatcher)
    }

    override suspend fun resumeRide(rideId: Long): Flow<Result<Unit>> =
        courseRemoteDataSource.resumeRide(rideId).map { result ->
            result.mapCatching {}
        }.flowOn(ioDispatcher)

    override suspend fun pauseRide(
        rideId: Long,
        duration: Long,
        totalDistance: Double,
    ): Flow<Result<Unit>> =
        courseRemoteDataSource.pauseRide(rideId, PauseRideDto(duration, totalDistance))
            .map { result ->
                result.mapCatching { }
            }.flowOn(ioDispatcher)

    override suspend fun addBookmark(courseId: Long): Flow<Result<Boolean>> =
        courseRemoteDataSource.addBookmark(courseId)
            .map { result ->
                result.mapCatching { it.data.isCourseBookmarked }
            }.flowOn(ioDispatcher)

    override suspend fun deleteBookmark(courseId: Long): Flow<Result<Boolean>> =
        courseRemoteDataSource.deleteBookmark(courseId)
            .map { result ->
                result.mapCatching { it.data.isCourseBookmarked }
            }.flowOn(ioDispatcher)
}