package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.ui.model.CourseDetailInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.CoursePage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface CourseRepository {
    suspend fun getRecentCourse(): Flow<Result<CourseListItem>>
    suspend fun getAllCourses(
        page: Int,
        status: String? = null,
    ): Flow<Result<CoursePage>>

    suspend fun getCourseDetail(courseName: String): Flow<Result<CourseDetailInfo>>
}

class CourseRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val courseRemoteDataSource: CourseRemoteDataSource,
) : CourseRepository {
    override suspend fun getRecentCourse(): Flow<Result<CourseListItem>> =
        courseRemoteDataSource.getRecentCourse().flowOn(ioDispatcher)

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
}