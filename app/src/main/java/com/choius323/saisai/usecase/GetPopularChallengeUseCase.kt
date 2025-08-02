package com.choius323.saisai.usecase

import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.model.CourseListItem
import kotlinx.coroutines.flow.Flow

class GetPopularChallengeUseCase(
    private val repository: CourseRepository,
) {
    suspend operator fun invoke(): Flow<Result<List<CourseListItem>>> =
        repository.getPopularChallenge()
}