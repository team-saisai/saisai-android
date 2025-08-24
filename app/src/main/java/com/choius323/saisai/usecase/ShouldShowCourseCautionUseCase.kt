package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate

class ShouldShowCourseCautionUseCase(
    private val repository: CourseRepository,
) {
    suspend operator fun invoke(localDate: LocalDate): Flow<Boolean> =
        repository.getLastShowDateCourseCaution().map { lastDate ->
            lastDate != localDate
        }
}

private const val TAG = "ShouldShowCourseCautionUseCase"