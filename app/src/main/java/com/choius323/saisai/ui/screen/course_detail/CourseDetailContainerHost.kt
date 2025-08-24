package com.choius323.saisai.ui.screen.course_detail

import com.choius323.saisai.ui.model.CourseDetail

data class CourseDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseDetail: CourseDetail? = null,
    val isShowCourseCaution: Boolean = false,
)

sealed interface CourseDetailUiEvent {
    data object CourseStartClicked : CourseDetailUiEvent
    data object BackClicked : CourseDetailUiEvent
    data class CourseCautionClickedButton(val isConfirm: Boolean, val isCheckNoMoreShow: Boolean) :
        CourseDetailUiEvent
}

sealed interface CourseDetailSideEffect {
    data class StartCourse(val courseId: Long) : CourseDetailSideEffect
    data object GoBack : CourseDetailSideEffect
    data class ShowToast(val message: String) : CourseDetailSideEffect
}