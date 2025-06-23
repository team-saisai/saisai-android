package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.ui.model.CourseInfo
import com.choius323.saisai.ui.model.CoursePage

interface CourseRepository {
    suspend fun getRecentCourse(): Result<CourseInfo>
    suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Result<CoursePage>
}

class CourseRepositoryImpl(
    private val courseRemoteDataSource: CourseRemoteDataSource,
) : CourseRepository {
    override suspend fun getRecentCourse(): Result<CourseInfo> {
        return courseRemoteDataSource.getRecentCourse()
    }

    override suspend fun getAllCourses(
        page: Int,
        level: Int?,
        distance: Int?,
        sigun: String?,
    ): Result<CoursePage> {
        return courseRemoteDataSource.getAllCourses(page, level, distance, sigun)
            .mapCatching { responseDto ->
                // DTO를 UI 모델로 변환
                responseDto.toCoursePage()
            }
    }
}