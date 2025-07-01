package com.choius323.saisai.ui.screen.course

import com.choius323.saisai.ui.model.CourseListItem

data class CourseListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseList: List<CourseListItem> = emptyList(),
)

sealed interface CourseListUiEvent {
    data class CourseClicked(val courseName: String) : CourseListUiEvent
}

sealed interface CourseListSideEffect {
    data class GoCourseDetail(val courseName: String) : CourseListSideEffect
}