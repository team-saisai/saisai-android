package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.CourseRemoteDataSource
import com.choius323.saisai.data.course.remote.model.CourseListResponseDto
import com.choius323.saisai.ui.model.CourseInfo
import com.choius323.saisai.ui.model.CourseListItem
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

// 확장 함수를 사용하여 DTO를 UI 모델로 변환
fun CourseListResponseDto.toCoursePage(): CoursePage {
    val content = this.data.content.map { courseItemDto ->
        CourseListItem(
            courseId = courseItemDto.courseId, // courseId가 String? 이므로 null 처리
            courseName = courseItemDto.courseName, // API 응답에 따라 기본값 또는 예외처리
            summary = courseItemDto.summary,
            level = courseItemDto.level, // 기본값 또는 적절한 예외 처리
            distance = courseItemDto.distance,
            estimatedTime = courseItemDto.estimatedTime,
            sigun = courseItemDto.sigun
        )
    } // content가 null이면 빈 리스트로 처리

    return CoursePage(
        content = content,
        totalElements = this.data.totalElements,
        totalPages = this.data.totalPages,
        currentPageNumber = this.data.number,
        pageSize = this.data.size,
        isFirstPage = this.data.first,
        isLastPage = this.data.last,
        isEmpty = this.data.empty
    )
}