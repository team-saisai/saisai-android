package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.ui.model.CourseDetailInfo
import com.choius323.saisai.ui.model.CoursePage
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RecentCourse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface CourseRepository {
    suspend fun getRecentCourse(): Flow<Result<RecentCourse?>>
    suspend fun getAllCourses(
        page: Int,
        status: String? = null,
    ): Flow<Result<CoursePage>>

    suspend fun getCourseDetail(courseName: String): Flow<Result<CourseDetailInfo>>
    suspend fun getPopularChallenge(): Flow<Result<List<PopularChallengeListItem>>>
}

class CourseRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val courseRemoteDataSource: CourseRemoteDataSource,
) : CourseRepository {
    override suspend fun getRecentCourse(): Flow<Result<RecentCourse?>> =
        courseRemoteDataSource.getRecentCourse().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data?.toRecentCourse()
            }
        }.flowOn(ioDispatcher)

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

    override suspend fun getCourseDetail(courseName: String): Flow<Result<CourseDetailInfo>> =
        courseRemoteDataSource.getCourseDetail(courseName).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toCourseDetailInfo()
            }
        }.flowOn(ioDispatcher)

    override suspend fun getPopularChallenge(): Flow<Result<List<PopularChallengeListItem>>> =
        courseRemoteDataSource.getPopularChallenge().map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.map { it.toPopularChallengeListItem() }
            }
        }.flowOn(ioDispatcher)
}