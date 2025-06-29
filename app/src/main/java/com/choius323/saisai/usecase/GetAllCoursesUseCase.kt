package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CoursePage
import kotlinx.coroutines.flow.Flow

class GetAllCoursesUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        page: Int,
        status: String? = null,
    ): Flow<Result<CoursePage>> = courseRepository.getAllCourses(page, status)
}