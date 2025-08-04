package com.choius323.saisai.ui.screen.bookmark_courses

import com.choius323.saisai.ui.model.CourseListItem

data class BookmarkCoursesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val editMode: Boolean = false,
    val courseList: List<CourseListItem> = emptyList(),
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val page: Int = 1,
    val selectedIndices: List<Int> = emptyList(),
    val showDeleteDialog: Boolean = false,
)

sealed interface BookmarkCoursesUiEvent {
    data object OnClickBack : BookmarkCoursesUiEvent
    data class OnClickCourse(val index: Int) : BookmarkCoursesUiEvent
    data class OnClickDeleteItem(val index: Int) : BookmarkCoursesUiEvent
    data object OnClickEdit : BookmarkCoursesUiEvent
    data object OnClickCancel : BookmarkCoursesUiEvent
    data object OnClickConfirm : BookmarkCoursesUiEvent
    data object OnClickDialogConfirm : BookmarkCoursesUiEvent
    data object OnClickDialogDismiss : BookmarkCoursesUiEvent
    data object OnClickDeleteAll : BookmarkCoursesUiEvent
    data object LoadMore : BookmarkCoursesUiEvent
}

sealed interface BookmarkCoursesSideEffect {
    data class ShowToast(val message: String) : BookmarkCoursesSideEffect
    data object GoBack : BookmarkCoursesSideEffect
}