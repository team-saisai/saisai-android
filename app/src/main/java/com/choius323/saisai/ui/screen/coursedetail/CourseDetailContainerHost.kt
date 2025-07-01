package com.choius323.saisai.ui.screen.coursedetail

import com.choius323.saisai.ui.model.CourseDetail

data class CourseDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseDetail: CourseDetail? = null,
)

sealed interface CourseDetailUiEvent {
    data object CourseStartClicked : CourseDetailUiEvent
    data object BackClicked : CourseDetailUiEvent
}

sealed interface CourseDetailSideEffect {
    data object StartCourse : CourseDetailSideEffect
    data object GoBack : CourseDetailSideEffect
}