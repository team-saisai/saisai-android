package com.choius323.saisai.ui.screen.course_list

import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.CourseSort
import com.choius323.saisai.ui.model.CourseType

data class CourseListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseList: List<CourseListItem> = emptyList(),
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val page: Int = 1,
    val selectedCourseType: CourseType = CourseType.challenge,
    val selectedSort: CourseSort = CourseSort.levelAsc,
)

sealed interface CourseListUiEvent {
    data class CourseClicked(val courseId: Long) : CourseListUiEvent
    data object LoadMore : CourseListUiEvent
    data class OnClickCourseType(val courseType: CourseType) : CourseListUiEvent
    data class OnClickSortType(val sort: CourseSort) : CourseListUiEvent
    data class OnClickBookmark(val courseId: Long, val isBookmarked: Boolean) : CourseListUiEvent
}

sealed interface CourseListSideEffect {
    data class GoCourseDetail(val courseId: Long) : CourseListSideEffect
    data class ShowToast(val message: String) : CourseListSideEffect
}