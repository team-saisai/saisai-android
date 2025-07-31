package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class ToggleBookmarkCourseUseCase(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: Long,
        isCurrentlyBookmarked: Boolean,
    ): Flow<Result<Boolean>> =
        if (isCurrentlyBookmarked) {
            courseRepository.deleteBookmark(courseId)
        } else {
            courseRepository.addBookmark(courseId)
        }
}