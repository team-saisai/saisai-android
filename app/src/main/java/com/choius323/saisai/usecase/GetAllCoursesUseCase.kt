package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CoursePage
import com.choius323.saisai.ui.model.CourseSort
import com.choius323.saisai.ui.model.CourseType
import kotlinx.coroutines.flow.Flow

class GetAllCoursesUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        page: Int, courseType: CourseType, sort: CourseSort,
    ): Flow<Result<CoursePage>> = courseRepository.getAllCourses(page, courseType, sort)
}