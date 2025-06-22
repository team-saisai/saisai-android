package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CoursePage

class GetAllCoursesUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        page: Int,
        level: Int? = null,
        distance: Int? = null,
        sigun: String? = null,
    ): Result<CoursePage> {
        return courseRepository.getAllCourses(page, level, distance, sigun)
    }
}