package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.ui.model.CourseDetailInfo
import com.choius323.saisai.ui.model.CourseInfo
import com.choius323.saisai.ui.model.CoursePage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface CourseRepository {
    suspend fun getRecentCourse(): Flow<Result<CourseInfo>>
    suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Flow<Result<CoursePage>>
    suspend fun getCourseDetail(courseName: String): Flow<Result<CourseDetailInfo>>
}

class CourseRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val courseRemoteDataSource: CourseRemoteDataSource,
) : CourseRepository {
    override suspend fun getRecentCourse(): Flow<Result<CourseInfo>> =
        courseRemoteDataSource.getRecentCourse().flowOn(ioDispatcher)

    override suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Flow<Result<CoursePage>> = withContext(ioDispatcher) {
        courseRemoteDataSource.getAllCourses(page, level, distance, sigun).map { result ->
            result.mapCatching { responseDto ->
                // DTO를 UI 모델로 변환
                responseDto.toCoursePage()
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getCourseDetail(courseName: String): Flow<Result<CourseDetailInfo>> =
        courseRemoteDataSource.getCourseDetail(courseName).map { result ->
            result.mapCatching { responseDto ->
                responseDto.data.toCourseDetailInfo() // DTO 내부의 data 객체에서 변환 함수 호출
            }
        }.flowOn(ioDispatcher) // UI 스레드 부담을 줄이기 위해 IO 디스패처에서 실행
}