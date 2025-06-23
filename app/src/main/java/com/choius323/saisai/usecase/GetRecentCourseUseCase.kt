package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CourseInfo
import kotlinx.coroutines.flow.Flow

class GetRecentCourseUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(): Flow<Result<CourseInfo>> = courseRepository.getRecentCourse()
}