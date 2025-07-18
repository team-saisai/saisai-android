package com.choius323.saisai.repository

import com.choius323.saisai.data.course.local.CourseLocalDataSource
import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
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
import java.io.File

interface CourseRepository {
    suspend fun getRecentCourse(): Flow<Result<RecentCourse?>>
    suspend fun getRecentRideCourse(): Flow<Result<RecentRide>>
    suspend fun getCourseDetail(courseId: Long): Flow<Result<CourseDetail>>
    suspend fun getPopularChallenge(): Flow<Result<List<PopularChallengeListItem>>>
    suspend fun startCourse(courseId: Long): Flow<Result<Long>>
    suspend fun setRecentRide(recentRide: RecentRide)
    suspend fun getRecentRide(): Flow<Result<RecentRide>>
    suspend fun getAllCourses(
        page: Int,
        status: String? = null,
    ): Flow<Result<CoursePage>>

    suspend fun completeCourse(
        rideId: Long, duration: Long, distance: Double, image: File,
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
                // DTO를 UI 모델로 변환
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
            result.mapCatching { responseDto -> responseDto.data }
        }.flowOn(ioDispatcher)

    override suspend fun completeCourse(
        rideId: Long, duration: Long, distance: Double, image: File,
    ): Flow<Result<Unit>> =
        courseRemoteDataSource.completeCourse(rideId, duration, distance, image).map { result ->
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
        }
    }
}