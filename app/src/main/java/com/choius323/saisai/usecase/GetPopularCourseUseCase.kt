package com.choius323.saisai.usecase

import com.choius323.saisai.ui.model.PopularCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularCourseUseCase {
    operator fun invoke(): Flow<Result<PopularCourse>> = flow {
        emit(Result.success(PopularCourse(emptyList())))
    }
}