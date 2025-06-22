package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CourseInfo

class GetRecentCourseUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(): Result<CourseInfo> {
        return courseRepository.getRecentCourse()
    }
}