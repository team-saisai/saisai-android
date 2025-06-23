package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CourseDetailInfo
import kotlinx.coroutines.flow.Flow

class GetCourseDetailUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(courseName: String): Flow<Result<CourseDetailInfo>> {
        return courseRepository.getCourseDetail(courseName)
    }
}